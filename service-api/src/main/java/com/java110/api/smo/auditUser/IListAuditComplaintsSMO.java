package com.java110.api.smo.auditUser;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 查询审核单
 */
public interface IListAuditComplaintsSMO {

    /**
     * 查询审核订单
     * @param pd
     * @return
     */
    public ResponseEntity<String> listAuditComplaints(IPageData pd);
}
