package com.java110.fee.bmo.feeDiscountRuleSpec;

import com.java110.po.feeDiscountRuleSpec.FeeDiscountRuleSpecPo;
import org.springframework.http.ResponseEntity;
public interface ISaveFeeDiscountRuleSpecBMO {


    /**
     * 添加折扣规则配置
     * add by wuxw
     * @param feeDiscountRuleSpecPo
     * @return
     */
    ResponseEntity<String> save(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo);


}
