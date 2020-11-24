package com.java110.fee.bmo.feeDiscount;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.feeDiscount.FeeDiscountPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeDiscountBMO {


    /**
     * 添加费用折扣
     * add by wuxw
     *
     * @param feeDiscountPo
     * @return
     */
    ResponseEntity<String> save(FeeDiscountPo feeDiscountPo, JSONArray feeDiscountRuleSpecs);


}
