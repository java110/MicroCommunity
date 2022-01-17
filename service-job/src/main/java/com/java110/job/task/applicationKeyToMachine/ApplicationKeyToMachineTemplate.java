package com.java110.job.task.applicationKeyToMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IApplicationKeyInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.dto.machine.ApplicationKeyDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
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
 * @ClassName ApplicationKeyToMachine
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/5 22:09
 * @Version 1.0
 * add by wuxw 2020/6/5
 **/
@Component
public class ApplicationKeyToMachineTemplate extends TaskSystemQuartz {

    Logger logger = LoggerFactory.getLogger(ApplicationKeyToMachineTemplate.class);


    private static final String TYPE_APPLICATION_KEY = "7788";

    private static final String STATE_NO_TRANSLATE = "10000";

    private static final String CREATE_FACE = "101"; //添加人脸

    private static final String DELETE_FACE = "102"; //删除人脸

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
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
            if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveMachineTranslate(tmpOrderDto, tmpMachineDto, applicationKeyDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveMachineTranslate(tmpOrderDto, tmpMachineDto, applicationKeyDto);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_INFO.equals(tmpOrderDto.getBusinessTypeCd())) {
                deleteMachineTranslate(tmpOrderDto, tmpMachineDto, applicationKeyDto);
            } else {

            }
        }

    }

    private void saveMachineTranslate(OrderDto tmpOrderDto, MachineDto tmpMachineDto, ApplicationKeyDto applicationKeyDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        machineTranslateDto.setTypeCd(TYPE_APPLICATION_KEY);
        machineTranslateDto.setObjId(applicationKeyDto.getApplicationKeyId());
        machineTranslateDto.setObjName(applicationKeyDto.getName());
        machineTranslateDto.setState(STATE_NO_TRANSLATE);
        machineTranslateDto.setCommunityId(applicationKeyDto.getCommunityId());
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setMachineCmd(CREATE_FACE);
        machineTranslateDto.setObjBId(tmpOrderDto.getbId());
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }


    private void deleteMachineTranslate(OrderDto tmpOrderDto, MachineDto tmpMachineDto, ApplicationKeyDto applicationKeyDto) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setMachineId(tmpMachineDto.getMachineId());
        machineTranslateDto.setMachineCode(tmpMachineDto.getMachineCode());
        machineTranslateDto.setTypeCd(TYPE_APPLICATION_KEY);
        machineTranslateDto.setObjId(applicationKeyDto.getApplicationKeyId());
        machineTranslateDto.setObjName(applicationKeyDto.getName());
        machineTranslateDto.setState(STATE_NO_TRANSLATE);
        machineTranslateDto.setCommunityId(applicationKeyDto.getCommunityId());
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setMachineCmd(DELETE_FACE);
        machineTranslateDto.setObjBId(tmpOrderDto.getbId());
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);

    }
}
