package com.java110.common.service.workflow;

import com.java110.dto.user.StaffDto;
import com.java110.dto.workflow.WorkflowDto;

public interface IQueryWorkFlowFirstStaffService {

    /**
     * 查询 工作流第一个员工信息
     * @param workflowDto
     * @return
     */
    StaffDto query(WorkflowDto workflowDto);
}
