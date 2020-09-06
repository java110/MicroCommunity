package com.java110.fee.bmo.meterWater.impl;

import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.fee.bmo.meterWater.IQueryPreMeterWater;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上期度数查询
 */
@Service
public class QueryPreMeterWaterImpl implements IQueryPreMeterWater {

    private static Logger logger = LoggerFactory.getLogger(QueryPreMeterWaterImpl.class);

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> query(MeterWaterDto meterWaterDto) {

        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        int total = meterWaterDtos == null ? 0 : meterWaterDtos.size();
        return ResultVo.createResponseEntity(1, total, meterWaterDtos);
    }
}
