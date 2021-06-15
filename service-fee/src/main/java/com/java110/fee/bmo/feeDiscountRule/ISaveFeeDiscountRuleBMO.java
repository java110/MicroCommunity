package com.java110.fee.bmo.feeDiscountRule;

import com.java110.po.feeDiscountRule.FeeDiscountRulePo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeDiscountRuleBMO {


    /**
     * 添加费用折扣规则
     * add by wuxw
     *
     * @param feeDiscountRulePo
     * @return
     */
    ResponseEntity<String> save(FeeDiscountRulePo feeDiscountRulePo);


}
