package com.java110.common.SmartMeter.factory.tqdianbiao;

import com.java110.common.SmartMeter.ISmartMeterFactoryAdapt;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.util.List;

/*
   拓强智能电表 处理类
   http://doc-api.tqdianbiao.com/#/api2/6/2/1
 */
@Service("tqDianBiaoFactoryAdaptImpl")
public class TqDianBiaoFactoryAdaptImpl implements ISmartMeterFactoryAdapt {
    @Override
    public ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree) {
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
