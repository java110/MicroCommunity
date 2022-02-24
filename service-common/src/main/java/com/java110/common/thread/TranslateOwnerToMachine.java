package com.java110.common.thread;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从订单中同步业主信息至设备中间表
 * add by wuxw 2019-11-14
 */
public class TranslateOwnerToMachine implements Runnable {
    Logger logger = LoggerFactory.getLogger(TranslateOwnerToMachine.class);
    public static final long DEFAULT_WAIT_SECOND = 5000 * 6; // 默认30秒执行一次
    public static boolean TRANSLATE_STATE = false;

    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    private IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    public TranslateOwnerToMachine(boolean state) {
        TRANSLATE_STATE = state;
        orderInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOrderInnerServiceSMO.class.getName(), IOrderInnerServiceSMO.class);
        ownerInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOwnerInnerServiceSMO.class.getName(), IOwnerInnerServiceSMO.class);
        ownerRoomRelInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOwnerRoomRelInnerServiceSMO.class.getName(), IOwnerRoomRelInnerServiceSMO.class);
        roomInnerServiceSMOImpl = ApplicationContextFactory.getBean(IRoomInnerServiceSMO.class.getName(), IRoomInnerServiceSMO.class);
        machineInnerServiceSMOImpl = ApplicationContextFactory.getBean("machineInnerServiceSMOImpl", IMachineInnerServiceSMO.class);
        machineTranslateServiceDaoImpl = ApplicationContextFactory.getBean("machineTranslateServiceDaoImpl", IMachineTranslateServiceDao.class);

    }

    @Override
    public void run() {
        long waitTime = DEFAULT_WAIT_SECOND;
        while (TRANSLATE_STATE) {
            try {
                executeTask();
                waitTime = StringUtil.isNumber(MappingCache.getValue("DEFAULT_WAIT_SECOND")) ?
                        Long.parseLong(MappingCache.getValue("DEFAULT_WAIT_SECOND")) : DEFAULT_WAIT_SECOND;
                Thread.sleep(waitTime);
            } catch (Throwable e) {
                logger.error("执行订单中同步业主信息至设备中失败", e);
            }
        }
    }

    /**
     * 执行任务
     */
    private void executeTask() {
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
                } else {
                    ownerDto.setbId(tmpOrderDto.getbId());
                }

                List<OwnerDto> ownerDtos = null;
                //根据bId 查询业主信息
                //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
                if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                    ownerDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
                    ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
                    MachineDto machineDto = new MachineDto();
                    machineDto.setMachineId("");
                    deleteMachineTranslate(machineDto, ownerDtos.get(0));
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1,当前为删除业主操作" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
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
                if ("9996".equals(tmpMachineDto.getMachineTypeCd())) {
                    continue;
                }
                if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                    saveMachineTranslate(tmpMachineDto, ownerDto);
//                } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
//                    updateMachineTranslate(tmpMachineDto, ownerDto);
                } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())
                        || BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ROOM_REL.equals(tmpOrderDto.getBusinessTypeCd())
                ) {
                    deleteMachineTranslate(tmpMachineDto, ownerDto);
                } else {

                }
            }

        }

    }

    private void saveMachineTranslate(MachineDto tmpMachineDto, OwnerDto ownerDto) {
        Map paramInfo = new HashMap();
        paramInfo.put("machineId", tmpMachineDto.getMachineId());
        paramInfo.put("objId", ownerDto.getMemberId());
        paramInfo.put("statusCd", "0");

        int count = machineTranslateServiceDaoImpl.queryMachineTranslatesCount(paramInfo);
        if (count > 0) {
            updateMachineTranslate(tmpMachineDto, ownerDto);
            return;
        }
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineTranslateId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("machineCode", tmpMachineDto.getMachineCode());
        info.put("typeCd", "8899");
        info.put("objId", ownerDto.getMemberId());
        info.put("objName", ownerDto.getName());
        info.put("state", "10000");
        info.put("communityId", ownerDto.getCommunityId());
        info.put("bId", "-1");
        machineTranslateServiceDaoImpl.saveMachineTranslate(info);

    }

    private void updateMachineTranslate(MachineDto tmpMachineDto, OwnerDto ownerDto) {
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("objId", ownerDto.getMemberId());
        info.put("state", "10000");
        info.put("communityId", ownerDto.getCommunityId());
        info.put("updateTime", new Date());
        machineTranslateServiceDaoImpl.updateMachineTranslate(info);

    }

    private void deleteMachineTranslate(MachineDto tmpMachineDto, OwnerDto ownerDto) {
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("objId", ownerDto.getMemberId());
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        info.put("communityId", ownerDto.getCommunityId());
        machineTranslateServiceDaoImpl.updateMachineTranslate(info);

    }

}
