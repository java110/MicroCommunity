package com.java110.store.bmo.collection;

import com.java110.dto.audit.AuditUser;
import org.springframework.http.ResponseEntity;

public interface IGetCollectionAuditOrderBMO {

    /**
     * 查询待审核单
     * @param auditUser
     * @return
     */
    ResponseEntity<String> auditOrder(AuditUser auditUser);
}
