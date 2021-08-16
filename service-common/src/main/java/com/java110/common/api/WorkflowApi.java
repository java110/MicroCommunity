package com.java110.common.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.dto.workflow.WorkflowDto;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/workflow")
public class WorkflowApi {

    @Autowired
    private IQueryWorkFlowFirstStaffBMO queryWorkFlowFirstStaffBMOImpl;

    @RequestMapping(value = "/getFirstStaff", method = RequestMethod.GET)
    public ResponseEntity<String> getFirstStaff(@RequestParam(name = "flowType") String flowType,
                                                @RequestParam(name = "communityId") String communityId,
                                                @RequestHeader(value = "store-id") String storeId) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(communityId);
        workflowDto.setFlowType(flowType);
        workflowDto.setStoreId(storeId);
        return queryWorkFlowFirstStaffBMOImpl.query(workflowDto);
    }






}
