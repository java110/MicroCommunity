package com.java110.store.bmo.collection;

import com.alibaba.fastjson.JSONObject;
import com.java110.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IGoodsCollectionBMO {

    /**
     * 物品领用
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> collection(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson);
}
