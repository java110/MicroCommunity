package com.java110.hardwareAdapation.thread;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.order.IOrderInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.dto.OwnerDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.dto.order.OrderDto;
import com.java110.hardwareAdapation.dao.IMachineTranslateServiceDao;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从订单中同步业主信息至设备中间表
 * add by wuxw 2019-11-14
 */
public class TranslateOwnerToMachineChangeMachine implements Runnable {
    Logger logger = LoggerFactory.getLogger(TranslateOwnerToMachineChangeMachine.class);
    public static final long DEFAULT_WAIT_SECOND = 5000 * 6; // 默认30秒执行一次
    public static boolean TRANSLATE_STATE = false;

    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    private IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    public TranslateOwnerToMachineChangeMachine(boolean state) {
        TRANSLATE_STATE = state;
        orderInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOrderInnerServiceSMO.class.getName(), IOrderInnerServiceSMO.class);
        ownerInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOwnerInnerServiceSMO.class.getName(), IOwnerInnerServiceSMO.class);
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
        MachineDto machineDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryMachineOrders(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                //根据bId 查询硬件信息
                machineDto = new MachineDto();
                machineDto.setbId(tmpOrderDto.getbId());
                List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
                if (machineDtos == null || machineDtos.size() == 0) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1"+ JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }
                dealData(tmpOrderDto, machineDtos.get(0));
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
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        for (OwnerDto tmpOwnerDto : ownerDtos) {
            if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveMachineTranslate(machineDto, tmpOwnerDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
                updateMachineTranslate(machineDto, tmpOwnerDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE.equals(tmpOrderDto.getBusinessTypeCd())) {
                deleteMachineTranslate(machineDto, tmpOwnerDto);
            } else {

            }
        }

    }

    private void saveMachineTranslate(MachineDto tmpMachineDto, OwnerDto ownerDto) {
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
