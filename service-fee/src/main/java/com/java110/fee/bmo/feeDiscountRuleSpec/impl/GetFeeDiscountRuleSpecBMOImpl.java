package com.java110.fee.bmo.feeDiscountRuleSpec.impl;

import com.java110.dto.fee.FeeDiscountRuleSpecDto;
import com.java110.fee.bmo.feeDiscountRuleSpec.IGetFeeDiscountRuleSpecBMO;
import com.java110.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountRuleSpecBMOImpl")
public class GetFeeDiscountRuleSpecBMOImpl implements IGetFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeDiscountRuleSpecDto feeDiscountRuleSpecDto) {


        int count = feeDiscountRuleSpecInnerServiceSMOImpl.queryFeeDiscountRuleSpecsCount(feeDiscountRuleSpecDto);

        List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecDtos = null;
        if (count > 0) {
            feeDiscountRuleSpecDtos = feeDiscountRuleSpecInnerServiceSMOImpl.queryFeeDiscountRuleSpecs(feeDiscountRuleSpecDto);
        } else {
            feeDiscountRuleSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountRuleSpecDto.getRow()), count, feeDiscountRuleSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
