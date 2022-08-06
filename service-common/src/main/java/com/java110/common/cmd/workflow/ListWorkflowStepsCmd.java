package com.java110.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowStepDto;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.common.IWorkflowStepInnerServiceSMO;
import com.java110.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "workflow.listWorkflowSteps")
public class ListWorkflowStepsCmd extends Cmd {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        Assert.listOnlyOne(workflowDtos,"查询条件错误");

        workflowDto = workflowDtos.get(0);

        WorkflowStepDto workflowStepDto = new WorkflowStepDto();
        workflowStepDto.setFlowId(workflowDto.getFlowId());

        List<WorkflowStepDto> workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);

        for(WorkflowStepDto tmpWorkflowStepDto : workflowStepDtos){
            WorkflowStepStaffDto workflowStepStaffDto = new WorkflowStepStaffDto();
            workflowStepStaffDto.setCommunityId(workflowDto.getCommunityId());
            workflowStepStaffDto.setStepId(tmpWorkflowStepDto.getStepId());
            List<WorkflowStepStaffDto> workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);
            tmpWorkflowStepDto.setWorkflowStepStaffs(workflowStepStaffDtos);
        }

        workflowDto.setWorkflowSteps(workflowStepDtos);

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, workflowDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
