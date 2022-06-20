package com.java110.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.common.IWorkflowV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "workflow.listWorkflowNextNode")
public class ListWorkflowNextNodeCmd extends Cmd {

    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务ID");
        Assert.hasKeyAndValue(reqJson, "startUserId", "未包含提交者");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject paramIn = new JSONObject();
        paramIn.put("taskId", reqJson.getString("taskId"));
        paramIn.put("startUserId", reqJson.getString("startUserId"));
        List<JSONObject> paramOuts = workflowV1InnerServiceSMOImpl.getWorkflowNextNode(paramIn);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOuts));
    }
}
