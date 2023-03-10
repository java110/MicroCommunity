package com.java110.common.smartMeter;

import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.vo.ResultVo;

import java.util.List;

/**
 * 智能电表 适配器
 */
public interface ISmartMeterFactoryAdapt {

    /**
     * 电表充值
     *
     * @param meterMachineDto
     * @param degree
     */
    ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree,double money);

    /**
     * 电表读数
     *
     * @param meterMachineDto
     * @return
     */
    ResultVo requestRead(MeterMachineDto meterMachineDto);

    /**
     * 多电表读数
     *
     * @param meterMachineDtos
     * @return
     */
    ResultVo requestReads(List<MeterMachineDto> meterMachineDtos);

    /**
     * 电表通知读取数据
     *
     * @param readData
     * @return
     */
    ResultVo notifyReadData(String readData);
}
