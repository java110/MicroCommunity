package com.java110.job.task.ownerToMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.ICommunityLocationInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityLocationDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.constant.BusinessTypeConstant;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ReTranslateOwnerPhotoMachine
 * @Description TODO 设备修改后 重新同步人脸信息
 * @Author wuxw
 * @Date 2020/6/5 21:52
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
@Component
public class ReTranslateOwnerPhotoMachineTemplate extends TaskSystemQuartz {

    Logger logger = LoggerFactory.getLogger(ReTranslateOwnerPhotoMachineTemplate.class);

    private static final String TYPE_OWNER = "8899";

    private static final String STATE_NO_TRANSLATE = "10000";

    private static final String CREATE_FACE = "101"; //添加人脸

    private static final String DELETE_FACE = "102"; //删除人脸
    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        MachineDto machineDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryMachineOrders(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));
            try {
                //根据bId 查询硬件信息
                machineDto = new MachineDto();
                machineDto.setbId(tmpOrderDto.getbId());
                List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
                if (machineDtos == null || machineDtos.size() == 0) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }
                if ("9999".equals(machineDtos.get(0).getMachineTypeCd())) {
                    dealData(tmpOrderDto, machineDtos.get(0));
                }

                //刷新 状态为C1
                orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
            } catch (Exception e) {
                logger.error("执行订单任务失败", e);
            }
        }
    }

    /**
     * 将业主数据同步给所有该小区设备
     *
     * @param tmpOrderDto
     * @param machineDto
     */
    private void dealData(OrderDto tmpOrderDto, MachineDto machineDto) {

        //拿到小区ID
        String communityId = machineDto.getCommunityId();

        //根据小区ID查询现有设备
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        List<OwnerDto> ownerDtos = null;
        String locationTypeCd = machineDto.getLocationTypeCd();
        CommunityLocationDto communityLocationDto = new CommunityLocationDto();
        communityLocationDto.setLocationId(locationTypeCd);
        communityLocationDto.setCommunityId(machineDto.getCommunityId());
        List<CommunityLocationDto> communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);

        if (communityLocationDtos == null || communityLocationDtos.size() < 1) {
            return;
        }
        communityLocationDto = communityLocationDtos.get(0);

        if ("1000".contains(communityLocationDto.getLocationType())) {//查询整个小区的业主
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else if ("2000".equals(communityLocationDto.getLocationType())) {//2000 单元门 ，则这个单元下的业主同步
            //先根据单元门ID 查询 房屋
            RoomDto roomDto = new RoomDto();
            roomDto.setUnitId(machineDto.getLocationObjId());
            roomDto.setCommunityId(machineDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() == 0) { // 单元下没有房屋
                return;
            }
            ownerDto.setRoomIds(getRoomIds(roomDtos));
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else if ("3000".equals(communityLocationDto.getLocationType())) {// 3000 房屋门
            ownerDto.setRoomId(machineDto.getLocationObjId());
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        }

        if (ownerDtos == null) {
            return;
        }
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())
                    || BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveMachineTranslate(tmpOrderDto, machineDto, tmpOwnerDto);
//            } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
//                updateMachineTranslate(machineDto, tmpOwnerDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
                deleteMachineTranslate(tmpOrderDto, machineDto, tmpOwnerDto);
            } else {

            }
        }

    }

    private String[] getRoomIds(List<RoomDto> roomDtos) {
        List<String> roomIds = new ArrayList<String>();
        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        return roomIds.toArray(new String[roomIds.size()]);
    }

    private void saveMachineTranslate(OrderDto tmpOrderDto, MachineDto tmpMachineDto, OwnerDto ownerDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        machineTranslateDto.setTypeCd(TYPE_OWNER);
        machineTranslateDto.setObjId(ownerDto.getMemberId());
        machineTranslateDto.setObjName(ownerDto.getName());
        machineTranslateDto.setState(STATE_NO_TRANSLATE);
        machineTranslateDto.setCommunityId(ownerDto.getCommunityId());
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setMachineCmd(CREATE_FACE);
        machineTranslateDto.setObjBId(tmpOrderDto.getbId());
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);

    }


    private void deleteMachineTranslate(OrderDto tmpOrderDto, MachineDto tmpMachineDto, OwnerDto ownerDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        machineTranslateDto.setTypeCd(TYPE_OWNER);
        machineTranslateDto.setObjId(ownerDto.getMemberId());
        machineTranslateDto.setObjName(ownerDto.getName());
        machineTranslateDto.setState(STATE_NO_TRANSLATE);
        machineTranslateDto.setCommunityId(ownerDto.getCommunityId());
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setMachineCmd(DELETE_FACE);
        machineTranslateDto.setObjBId(tmpOrderDto.getbId());
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);

    }
}
