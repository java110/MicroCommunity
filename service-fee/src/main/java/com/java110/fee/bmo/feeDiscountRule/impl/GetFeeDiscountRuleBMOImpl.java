package com.java110.fee.bmo.feeDiscountRule.impl;

import com.java110.dto.feeDiscountRule.FeeDiscountRuleDto;
import com.java110.fee.bmo.feeDiscountRule.IGetFeeDiscountRuleBMO;
import com.java110.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountRuleBMOImpl")
public class GetFeeDiscountRuleBMOImpl implements IGetFeeDiscountRuleBMO {

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeDiscountRuleDto feeDiscountRuleDto) {


        int count = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRulesCount(feeDiscountRuleDto);

        List<FeeDiscountRuleDto> feeDiscountRuleDtos = null;
        if (count > 0) {
            feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);
        } else {
            feeDiscountRuleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountRuleDto.getRow()), count, feeDiscountRuleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
