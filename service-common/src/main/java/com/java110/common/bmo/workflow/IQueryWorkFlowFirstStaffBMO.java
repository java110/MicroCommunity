package com.java110.common.bmo.workflow;

import com.java110.dto.user.StaffDto;
import com.java110.dto.workflow.WorkflowDto;

public interface IQueryWorkFlowFirstStaffBMO {

    /**
     * 查询 工作流第一个员工信息
     * @param workflowDto
     * @return
     */
    StaffDto query(WorkflowDto workflowDto);
}
