package com.java110.common.bmo.workflow;

import com.java110.dto.oaWorkflow.WorkflowDto;
import com.java110.dto.oaWorkflow.WorkflowModelDto;
import org.springframework.http.ResponseEntity;

public interface IQueryWorkFlowFirstStaffBMO {

    /**
     * 查询 工作流第一个员工信息
     * @param workflowDto
     * @return
     */
    ResponseEntity<String> query(WorkflowDto workflowDto);

    ResponseEntity<String> deployModel(WorkflowModelDto workflowModelDto);

    ResponseEntity<String> saveModel(WorkflowModelDto workflowModelDto);
}
