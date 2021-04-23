package com.java110.store.bmo.purchase;

import com.java110.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IResourceEnterBMO {

    /**
     * 采购入库
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> enter(PurchaseApplyPo purchaseApplyPo);
}
