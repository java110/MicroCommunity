package com.java110.job.task.ownerToMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.community.ICommunityLocationInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TransalateOwnerPhotoToMachine
 * @Description TODO 传输业主图片到门禁 任务
 * @Author wuxw
 * @Date 2020/6/3 20:59
 * @Version 1.0
 * add by wuxw 2020/6/3
 **/
@Component
public class TranslateOwnerPhotoToMachineTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(TranslateOwnerPhotoToMachineTemplate.class);

    private static final String TYPE_OWNER = "8899";

    private static final String STATE_NO_TRANSLATE = "10000";

    private static final String CREATE_FACE = "101"; //添加人脸

    private static final String DELETE_FACE = "102"; //删除人脸

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        logger.debug("任务在执行" + taskDto.toString());

        OwnerDto ownerDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOwenrOrders(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));
                ownerDto = new OwnerDto();
                if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())) {
                    //判断是否为添加房屋
                    OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                    ownerRoomRelDto.setbId(tmpOrderDto.getbId());
                    List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
                    Assert.listOnlyOne(ownerRoomRelDtos, "数据错误 业主房屋关系未找到，或找到多条" + JSONObject.toJSONString(tmpOrderDto));
                    ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())) {
                    OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                    ownerRoomRelDto.setbId(tmpOrderDto.getbId());
                    ownerRoomRelDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
                    List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

                    if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
                        //刷新 状态为C1
                        orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                        logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                        continue;
                    }
                    ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                } else {
                    ownerDto.setbId(tmpOrderDto.getbId());
                }

                List<OwnerDto> ownerDtos = null;
                //根据bId 查询业主信息
                //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
                if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                    ownerDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
                }

                ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

                // 房屋信息
                if (ownerDtos == null || ownerDtos.size() == 0) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }
                RoomDto roomDto = new RoomDto();
                roomDto.setOwnerId(ownerDtos.get(0).getOwnerId());
                //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
                if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())) {
                    roomDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
                }
                List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

                dealData(tmpOrderDto, ownerDtos, rooms);
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
     * @param ownerDtos
     */
    private void dealData(OrderDto tmpOrderDto, List<OwnerDto> ownerDtos, List<RoomDto> roomDtos) {

        //拿到小区ID
        String communityId = ownerDtos.get(0).getCommunityId();
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

        for (OwnerDto ownerDto : ownerDtos) {

            for (MachineDto tmpMachineDto : machineDtos) {
                if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                    continue;
                }
                if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                    saveMachineTranslate(tmpOrderDto, tmpMachineDto, ownerDto);
//                } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
//                    updateMachineTranslate(tmpMachineDto, ownerDto);
                } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())
                ) {
                    deleteMachineTranslate(tmpOrderDto, tmpMachineDto, ownerDto);
                } else {

                }
            }

        }

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
