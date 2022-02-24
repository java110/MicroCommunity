package com.java110.common.thread;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.order.OrderDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从订单中同步业主信息至设备中间表
 * add by wuxw 2019-11-14
 */
public class TranslateApplicationKeyToMachine implements Runnable {
    Logger logger = LoggerFactory.getLogger(TranslateApplicationKeyToMachine.class);
    public static final long DEFAULT_WAIT_SECOND = 5000 * 6; // 默认30秒执行一次
    public static boolean TRANSLATE_STATE = false;

    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    private IMachineTranslateServiceDao machineTranslateServiceDaoImpl;

    public TranslateApplicationKeyToMachine(boolean state) {
        TRANSLATE_STATE = state;
        orderInnerServiceSMOImpl = ApplicationContextFactory.getBean(IOrderInnerServiceSMO.class.getName(), IOrderInnerServiceSMO.class);
        roomInnerServiceSMOImpl = ApplicationContextFactory.getBean(IRoomInnerServiceSMO.class.getName(), IRoomInnerServiceSMO.class);
        machineInnerServiceSMOImpl = ApplicationContextFactory.getBean("machineInnerServiceSMOImpl", IMachineInnerServiceSMO.class);
        machineTranslateServiceDaoImpl = ApplicationContextFactory.getBean("machineTranslateServiceDaoImpl", IMachineTranslateServiceDao.class);
        applicationKeyInnerServiceSMOImpl = ApplicationContextFactory.getBean("applicationKeyInnerServiceSMOImpl", IApplicationKeyInnerServiceSMO.class);

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
        ApplicationKeyDto applicationKeyDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryApplicationKeyOrders(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));
                //根据bId 查询业主信息
                applicationKeyDto = new ApplicationKeyDto();
                applicationKeyDto.setbId(tmpOrderDto.getbId());
                List<ApplicationKeyDto> applicationKeyDtos = applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto);

                // 房屋信息
                if (applicationKeyDtos == null || applicationKeyDtos.size() == 0) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }

                if (!"10001".equals(applicationKeyDto.getState())) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("数据状态不是审核完成直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                }

                dealData(tmpOrderDto, applicationKeyDtos.get(0));
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
     * @param applicationKeyDto
     */
    private void dealData(OrderDto tmpOrderDto, ApplicationKeyDto applicationKeyDto) {

        //拿到小区ID
        String communityId = applicationKeyDto.getCommunityId();

        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        //String[] locationObjIds = new String[]{communityId};
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);

        machineDto.setMachineId(applicationKeyDto.getMachineId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        for (MachineDto tmpMachineDto : machineDtos) {
            if ("9996".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveMachineTranslate(tmpMachineDto, applicationKeyDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                updateMachineTranslate(tmpMachineDto, applicationKeyDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                deleteMachineTranslate(tmpMachineDto, applicationKeyDto);
            } else {

            }
        }

    }

    private void saveMachineTranslate(MachineDto tmpMachineDto, ApplicationKeyDto applicationKeyDto) {
        Map paramInfo = new HashMap();
        paramInfo.put("machineId", tmpMachineDto.getMachineId());
        paramInfo.put("objId", applicationKeyDto.getApplicationKeyId());

        int count = machineTranslateServiceDaoImpl.queryMachineTranslatesCount(paramInfo);
        if (count > 0) {
            updateMachineTranslate(tmpMachineDto, applicationKeyDto);
            return;
        }
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineTranslateId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("machineCode", tmpMachineDto.getMachineCode());
        info.put("typeCd", "7788");
        info.put("objId", applicationKeyDto.getApplicationKeyId());
        info.put("objName", applicationKeyDto.getName());
        info.put("state", "10000");
        info.put("communityId", applicationKeyDto.getCommunityId());
        info.put("bId", "-1");
        machineTranslateServiceDaoImpl.saveMachineTranslate(info);

    }

    private void updateMachineTranslate(MachineDto tmpMachineDto, ApplicationKeyDto applicationKeyDto) {
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("objId", applicationKeyDto.getApplicationKeyId());
        info.put("state", "10000");
        info.put("communityId", applicationKeyDto.getCommunityId());
        machineTranslateServiceDaoImpl.updateMachineTranslate(info);

    }

    private void deleteMachineTranslate(MachineDto tmpMachineDto, ApplicationKeyDto applicationKeyDto) {
        Map info = new HashMap();
        //machine_id,machine_code,status_cd,type_cd,machine_translate_id,obj_id,obj_name,state,community_id,b_id
        info.put("machineId", tmpMachineDto.getMachineId());
        info.put("objId", applicationKeyDto.getApplicationKeyId());
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        info.put("communityId", applicationKeyDto.getCommunityId());
        machineTranslateServiceDaoImpl.updateMachineTranslate(info);

    }

}
