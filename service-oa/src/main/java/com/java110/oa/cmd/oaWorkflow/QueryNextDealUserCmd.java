package com.java110.oa.cmd.oaWorkflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 下一步处理人查询
 */
@Java110Cmd(serviceCode = "oaWorkflow.queryNextDealUser")
public class QueryNextDealUserCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "startUserId", "未包含创建人");
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        List<JSONObject> tasks = oaWorkflowUserInnerServiceSMOImpl.nextAllNodeTaskList(reqJson);
        context.setResponseEntity(ResultVo.createResponseEntity(tasks));
    }
}
