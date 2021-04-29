package com.java110.store.bmo.collection;

import com.java110.entity.audit.AuditUser;
import com.java110.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IGetCollectionAuditOrderBMO {

    /**
     * 查询待审核单
     * @param auditUser
     * @return
     */
    ResponseEntity<String> auditOrder(AuditUser auditUser);
}
