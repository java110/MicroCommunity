package com.java110.fee.bmo.rentingFee.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.bmo.rentingFee.IQueryRentingFee;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("queryRentingFeeImpl")
public class QueryRentingFeeImpl implements IQueryRentingFee {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> queryFees(FeeDto feeDto) {

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return ResultVo.createResponseEntity(new JSONArray());
        }

        return ResultVo.createResponseEntity(feeDtos);
    }
}
