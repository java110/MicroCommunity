package com.java110.fee.bmo.feeDiscountSpec.impl;

import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.bmo.feeDiscountSpec.IGetFeeDiscountSpecBMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountSpecBMOImpl")
public class GetFeeDiscountSpecBMOImpl implements IGetFeeDiscountSpecBMO {

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeDiscountSpecDto feeDiscountSpecDto) {


        int count = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecsCount(feeDiscountSpecDto);

        List<FeeDiscountSpecDto> feeDiscountSpecDtos = null;
        if (count > 0) {
            feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(feeDiscountSpecDto);
        } else {
            feeDiscountSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountSpecDto.getRow()), count, feeDiscountSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
