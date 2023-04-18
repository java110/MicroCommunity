package com.java110.oa.cmd.oaWorkflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询 提交---> 审核人
 */
@Java110Cmd(serviceCode = "oaWorkflow.queryFirstAuditStaff")
public class QueryFirstAuditStaffCmd extends Cmd {

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setFlowId(reqJson.getString("flowId"));
        oaWorkflowDto.setStoreId(storeId);
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        OaWorkflowXmlDto oaWorkflowXmlDto = new OaWorkflowXmlDto();
        oaWorkflowXmlDto.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        List<OaWorkflowXmlDto> oaWorkflowXmlDtos = oaWorkflowXmlInnerServiceSMOImpl.queryOaWorkflowXmls(oaWorkflowXmlDto);
        Assert.listOnlyOne(oaWorkflowXmlDtos, "流程不存在");

        List<JSONObject> tasks = oaWorkflowActivitiInnerServiceSMOImpl.queryFirstAuditStaff(oaWorkflowXmlDtos.get(0));

        context.setResponseEntity(ResultVo.createResponseEntity(tasks));

    }
}
