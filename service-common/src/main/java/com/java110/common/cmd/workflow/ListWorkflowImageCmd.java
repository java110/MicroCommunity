package com.java110.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "workflow.listWorkflowImage")
public class ListWorkflowImageCmd extends Cmd {
    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中请包含小区ID");
        Assert.hasKeyAndValue(reqJson, "flowId", "请求报文中请包含商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);


        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        Assert.listOnlyOne(workflowDtos,"未查到工作流");

        workflowDto = workflowDtos.get(0);
        ResultVo resultVo = null;
        if(StringUtil.isEmpty(workflowDto.getProcessDefinitionKey())){
            resultVo = new ResultVo(ResultVo.CODE_ERROR, "流程还没有部署");
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        String image = workflowInnerServiceSMOImpl.getWorkflowImage(workflowDto);

        if(StringUtil.isEmpty(workflowDto.getProcessDefinitionKey())){
            resultVo = new ResultVo(ResultVo.CODE_ERROR, "查询流程错误");
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, image);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }
}
