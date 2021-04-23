package com.java110.store.bmo.collection;

import com.java110.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IResourceOutBMO {

    /**
     * 采购入库
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> out(PurchaseApplyPo purchaseApplyPo);
}
