package com.java110.common.bmo.workflowAuditMessage;

import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteWorkflowAuditMessageBMO {


    /**
     * 修改流程审核表
     * add by wuxw
     *
     * @param workflowAuditMessagePo
     * @return
     */
    ResponseEntity<String> delete(WorkflowAuditMessagePo workflowAuditMessagePo);


}
