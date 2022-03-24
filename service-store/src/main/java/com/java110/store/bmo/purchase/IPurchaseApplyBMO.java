package com.java110.store.bmo.purchase;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IPurchaseApplyBMO {

    /**
     * 采购申请
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> apply(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson);
}
