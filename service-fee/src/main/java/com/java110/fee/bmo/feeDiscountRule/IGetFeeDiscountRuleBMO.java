package com.java110.fee.bmo.feeDiscountRule;

import com.java110.dto.fee.FeeDiscountRuleDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeDiscountRuleBMO {


    /**
     * 查询费用折扣规则
     * add by wuxw
     *
     * @param feeDiscountRuleDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountRuleDto feeDiscountRuleDto);


}
