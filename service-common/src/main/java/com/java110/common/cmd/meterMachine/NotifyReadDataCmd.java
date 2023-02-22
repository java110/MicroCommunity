package com.java110.common.cmd.meterMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.SmartMeter.ISmartMeterFactoryAdapt;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.meterMachineFactory.MeterMachineFactoryDto;
import com.java110.intf.common.IMeterMachineFactoryV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "meterMachine.notifyReadData")
public class NotifyReadDataCmd extends Cmd {

    private ISmartMeterFactoryAdapt smartMeterFactoryAdapt;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineFactoryV1InnerServiceSMO meterMachineFactoryV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "machineId", "未包含设备");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setMachineId(reqJson.getString("machineId"));
        List<MeterMachineDto> meterMachines = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);

        Assert.listOnlyOne(meterMachines,"智能水电表不存在");


        MeterMachineFactoryDto meterMachineFactoryDto = new MeterMachineFactoryDto();
        meterMachineFactoryDto.setFactoryId(meterMachines.get(0).getImplBean());
        List<MeterMachineFactoryDto> meterMachineFactoryDtos = meterMachineFactoryV1InnerServiceSMOImpl.queryMeterMachineFactorys(meterMachineFactoryDto);
        Assert.listOnlyOne(meterMachineFactoryDtos,"智能水电表厂家不存在");

        smartMeterFactoryAdapt = ApplicationContextFactory.getBean(meterMachineFactoryDtos.get(0).getBeanImpl(),ISmartMeterFactoryAdapt.class);
        if(smartMeterFactoryAdapt == null){
            throw new CmdException("厂家接口未实现");
        }

        // 通知 厂家适配器数据
        smartMeterFactoryAdapt.notifyReadData(reqJson.toJSONString());
    }
}
