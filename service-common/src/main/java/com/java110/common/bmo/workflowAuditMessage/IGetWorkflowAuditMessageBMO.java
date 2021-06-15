package com.java110.common.bmo.workflowAuditMessage;
import com.java110.dto.workflowAuditMessage.WorkflowAuditMessageDto;
import org.springframework.http.ResponseEntity;
public interface IGetWorkflowAuditMessageBMO {


    /**
     * 查询流程审核表
     * add by wuxw
     * @param  workflowAuditMessageDto
     * @return
     */
    ResponseEntity<String> get(WorkflowAuditMessageDto workflowAuditMessageDto);


}
