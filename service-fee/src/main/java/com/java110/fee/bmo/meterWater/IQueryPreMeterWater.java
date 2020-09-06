package com.java110.fee.bmo.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.meterWater.MeterWaterDto;
import org.springframework.http.ResponseEntity;

/**
 * 查询上期信息
 */
public interface IQueryPreMeterWater {

    /**
     * 查询记录
     * @param meterWaterDto 表信息
     * @return
     */
    ResponseEntity<String> query(MeterWaterDto meterWaterDto);
}
