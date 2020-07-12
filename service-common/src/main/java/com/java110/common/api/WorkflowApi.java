package com.java110.common.api;

import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.dto.user.StaffDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.workflow.IWorkflowApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowApi implements IWorkflowApi {

    @Autowired
    private IQueryWorkFlowFirstStaffBMO queryWorkFlowFirstStaffServiceImpl;
    @Override
    public StaffDto getFirstStaff(@RequestParam(name = "flowType") String flowType,
                                  @RequestParam(name = "communityId") String communityId) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(communityId);
        workflowDto.setFlowType(flowType);
        return queryWorkFlowFirstStaffServiceImpl.query(workflowDto);
    }
}
