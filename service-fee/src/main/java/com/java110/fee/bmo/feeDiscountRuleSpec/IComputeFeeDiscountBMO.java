package com.java110.fee.bmo.feeDiscountRuleSpec;

import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface IComputeFeeDiscountBMO {

    /**
     * 查询折扣规则配置
     * add by wuxw
     *
     * @return
     */
    ResponseEntity<String> compute(String feeId, String communityId, double cycles, String payerObjId, String payerObjType, String endTime, int page, int row) throws ParseException;

}
