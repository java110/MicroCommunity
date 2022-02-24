package com.java110.job.task.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.constant.BusinessTypeConstant;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class CarToMachineTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(CarToMachineTemplate.class);

    private static final String TYPE_OWNER_CAR = "4455";

    private static final String STATE_NO_TRANSLATE = "10000";

    private static final String CREATE_OWNER_CAR = "201"; //添加

    private static final String DELETE_OWNER_CAR = "202"; //删除

    private static final String UPDATE_OWNER_CAR = "203"; //修改

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        logger.debug("任务在执行" + taskDto.toString());

        OwnerCarDto ownerCarDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        orderDto.setBusinessTypeCds(new String[]{BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR,
                BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR,
                BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR});
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBusinessType(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                logger.debug("开始处理订单" + JSONObject.toJSONString(tmpOrderDto));
                ownerCarDto = new OwnerCarDto();

                ownerCarDto.setbId(tmpOrderDto.getbId());

                if(BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR.equals(tmpOrderDto.getBusinessTypeCd())){
                    ownerCarDto.setStatusCd("1");
                }

                List<OwnerCarDto> ownerCarDtos = null;


                ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

                // 房屋信息
                if (ownerCarDtos == null || ownerCarDtos.size() == 0) {
                    //刷新 状态为C1
                    orderInnerServiceSMOImpl.updateBusinessStatusCd(tmpOrderDto);
                    logger.debug("没有数据数据直接刷为C1" + JSONObject.toJSONString(tmpOrderDto));
                    continue;
                }

                dealData(tmpOrderDto, ownerCarDtos);
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
     * @param ownerCarDtos
     */
    private void dealData(OrderDto tmpOrderDto, List<OwnerCarDto> ownerCarDtos) {

        //拿到小区ID
        String communityId = ownerCarDtos.get(0).getCommunityId();
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        machineDto.setMachineTypeCd(MachineDto.MACHINE_TYPE_CAR);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }
        //根据小区ID查询现有设备


        for (OwnerCarDto ownerCarDto : ownerCarDtos) {

            if (BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_CAR.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveOrUpdateMachineTranslate(tmpOrderDto, ownerCarDto, machineDtos, CREATE_OWNER_CAR);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_CAR.equals(tmpOrderDto.getBusinessTypeCd())) {
                saveOrUpdateMachineTranslate(tmpOrderDto, ownerCarDto, machineDtos, UPDATE_OWNER_CAR);
            } else if (BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR.equals(tmpOrderDto.getBusinessTypeCd())
            ) {
                deleteMachineTranslate(tmpOrderDto, ownerCarDto, machineDtos);
            } else {

            }


        }

    }

    private void saveOrUpdateMachineTranslate(OrderDto tmpOrderDto, OwnerCarDto ownerCarDto, List<MachineDto> machineDtos, String cmd) {

        for (MachineDto machineDto : machineDtos) {
            MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
            machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
            machineTranslateDto.setMachineId(machineDto.getMachineId());
            machineTranslateDto.setMachineCode(machineDto.getMachineCode());
            machineTranslateDto.setTypeCd(TYPE_OWNER_CAR);
            machineTranslateDto.setObjId(ownerCarDto.getCarId());
            machineTranslateDto.setObjName(ownerCarDto.getCarNum());
            machineTranslateDto.setState(STATE_NO_TRANSLATE);
            machineTranslateDto.setCommunityId(ownerCarDto.getCommunityId());
            machineTranslateDto.setbId("-1");
            machineTranslateDto.setMachineCmd(cmd);
            machineTranslateDto.setObjBId(tmpOrderDto.getbId());
            machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
        }

    }

    private void deleteMachineTranslate(OrderDto tmpOrderDto, OwnerCarDto ownerCarDto, List<MachineDto> machineDtos) {
        for (MachineDto machineDto : machineDtos) {
            MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
            machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
            machineTranslateDto.setMachineId(machineDto.getMachineId());
            machineTranslateDto.setMachineCode(machineDto.getMachineCode());
            machineTranslateDto.setTypeCd(TYPE_OWNER_CAR);
            machineTranslateDto.setObjId(ownerCarDto.getCarId());
            machineTranslateDto.setObjName(ownerCarDto.getCarNum());
            machineTranslateDto.setState(STATE_NO_TRANSLATE);
            machineTranslateDto.setCommunityId(ownerCarDto.getCommunityId());
            machineTranslateDto.setbId("-1");
            machineTranslateDto.setMachineCmd(DELETE_OWNER_CAR);
            machineTranslateDto.setObjBId(tmpOrderDto.getbId());
            machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
        }
    }
}
