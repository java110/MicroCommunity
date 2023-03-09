package com.java110.common.smartMeter.factory.common;

import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.util.List;

/*
   通用 电表接口类

   推荐其他智能电表实现
 */
@Service("commonFactoryAdaptImpl")
public class CommonFactoryAdaptImpl implements ISmartMeterFactoryAdapt {
    @Override
    public ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree, double money) {
        return null;
    }

    @Override
    public ResultVo requestRead(MeterMachineDto meterMachineDto) {
        return null;
    }

    @Override
    public ResultVo requestReads(List<MeterMachineDto> meterMachineDtos) {
        return null;
    }

    @Override
    public ResultVo notifyReadData(String readData) {
        return null;
    }
}
