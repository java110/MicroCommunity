package com.java110.common.smartMeter;

import com.java110.dto.meterMachineDetail.MeterMachineDetailDto;

/**
 * 水电抄表逻辑
 */
public interface ISmartMeterCoreRead {

    /**
     * 抄表
     */
    void saveMeterAndCreateFee(MeterMachineDetailDto meterMachineDetailDto, String degree,String batchId);

    String generatorBatch(String communityId);
}
