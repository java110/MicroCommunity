package com.java110.job.task.visiterToMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.visit.VisitDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TransalateOwnerPhotoToMachine
 * @Description TODO 传输访客图片到门禁 任务
 * @Author wuxw
 * @Date 2020/6/3 20:59
 * @Version 1.0
 * add by wuxw 2020/6/3
 **/
@Component
public class TranslateVisitPhotoToMachineTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(TranslateVisitPhotoToMachineTemplate.class);

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        logger.debug("任务在执行" + taskDto.toString());

        OrderDto orderDto = new OrderDto();
        String[] businessTypeCds = new String[]{"120100030001"};
        orderDto.setBusinessTypeCds(businessTypeCds);
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBusinessType(orderDto);

        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));

                VisitDto visitDto = new VisitDto();
                visitDto.setbId(tmpOrderDto.getbId());
                List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);

                if (visitDtos == null || visitDtos.size() < 1) {
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }

                RoomDto roomDto = new RoomDto();
                roomDto.setOwnerId(visitDtos.get(0).getOwnerId());
                List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

                dealData(tmpOrderDto, visitDtos.get(0), rooms);
                //刷新 状态为C1
                orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                logger.debug("处理订单结束" + JSONObject.toJSONString(tmpOrderDto));

            } catch (Exception e) {
                logger.error("执行订单任务失败", e);
            }
        }

    }

    /**
     * 将业主数据同步给所有该小区设备
     *
     * @param tmpOrderDto
     * @param visitDto
     */
    private void dealData(OrderDto tmpOrderDto, VisitDto visitDto, List<RoomDto> roomDtos) {

        //拿到小区ID
        String communityId = visitDto.getCommunityId();
        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        //String[] locationObjIds = new String[]{communityId};
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);
        for (RoomDto roomDto : roomDtos) {
            locationObjIds.add(roomDto.getUnitId());
            locationObjIds.add(roomDto.getRoomId());
        }
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);


        for (MachineDto tmpMachineDto : machineDtos) {
            if ("9996".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            saveMachineTranslate(tmpMachineDto, visitDto);
        }


    }


    private void saveMachineTranslate(MachineDto tmpMachineDto, VisitDto visitDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();


        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setObjId(visitDto.getvId());
        machineTranslateDto.setStatusCd("0");

        int count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);
        if (count > 0) {
            updateMachineTranslate(tmpMachineDto, visitDto);
            return;
        }
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        MachineTranslateDto tmpMachineTranslateDto = new MachineTranslateDto();
        tmpMachineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        tmpMachineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        tmpMachineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        tmpMachineTranslateDto.setTypeCd("6677"); //访客人脸
        tmpMachineTranslateDto.setObjId(visitDto.getvId());
        tmpMachineTranslateDto.setObjName(visitDto.getvName());
        tmpMachineTranslateDto.setState("10000");
        tmpMachineTranslateDto.setCommunityId(visitDto.getCommunityId());
        tmpMachineTranslateDto.setbId("-1");
        tmpMachineTranslateDto.setMachineCmd("101");
        tmpMachineTranslateDto.setObjBId("-1");
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(tmpMachineTranslateDto);

    }

    private void updateMachineTranslate(MachineDto tmpMachineDto, VisitDto visitDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setObjId(visitDto.getvId());
        machineTranslateDto.setState("10000");
        machineTranslateDto.setCommunityId(visitDto.getCommunityId());
        machineTranslateDto.setUpdateTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(machineTranslateDto);

    }
}
