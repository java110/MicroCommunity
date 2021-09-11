package com.java110.api.smo.auditUser;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

public interface IAuditOrdersSMO {

    ResponseEntity<String> auditOrder(IPageData pd) throws SMOException;
}
