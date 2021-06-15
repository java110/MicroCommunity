package com.java110.fee.bmo.feeDiscount;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.feeDiscount.FeeDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeDiscountBMO {


    /**
     * 修改费用折扣
     * add by wuxw
     *
     * @param feeDiscountPo
     * @return
     */
    ResponseEntity<String> update(FeeDiscountPo feeDiscountPo, JSONArray feeDiscountRuleSpecs);


}
