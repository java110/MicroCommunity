package com.java110.user.cmd.ownerSettled;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerSettledApplyDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.user.IOwnerSettledApplyV1InnerServiceSMO;
import com.java110.intf.user.IOwnerSettledSettingV1InnerServiceSMO;
import com.java110.po.ownerSettledApply.OwnerSettledApplyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 审核 放行
 */
@Java110Cmd(serviceCode = "ownerSettled.auditUndoOwnerSettled")
public class AuditUndoOwnerSettledCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;


    @Autowired
    private IOwnerSettledApplyV1InnerServiceSMO ownerSettledApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerSettledSettingV1InnerServiceSMO ownerSettledSettingV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "applyId", "未包含applyId");
        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含状态说明");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        OwnerSettledApplyDto ownerSettledApplyDto = new OwnerSettledApplyDto();
        ownerSettledApplyDto.setApplyId(reqJson.getString("applyId"));
        List<OwnerSettledApplyDto> ownerSettledApplyDtos = ownerSettledApplyV1InnerServiceSMOImpl.queryOwnerSettledApplys(ownerSettledApplyDto);

        Assert.listOnlyOne(ownerSettledApplyDtos, "入驻申请不存在");

        //状态 W待审核 D 审核中 C 审核完成 D 审核失败
        OwnerSettledApplyPo ownerSettledApplyPo = new OwnerSettledApplyPo();
        ownerSettledApplyPo.setApplyId(ownerSettledApplyDtos.get(0).getApplyId());
        reqJson.put("id",reqJson.getString("applyId"));
        reqJson.put("storeId",storeId);


        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))
                || "1500".equals(reqJson.getString("auditCode"))) { //办理操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                ownerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_COMPLETE);
            } else {
                ownerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_DOING);
            }
            ownerSettledApplyV1InnerServiceSMOImpl.updateOwnerSettledApply(ownerSettledApplyPo);
            //完成当前流程 插入下一处理人
        } else if ("1300".equals(reqJson.getString("auditCode"))) { //转单操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            //reqJson.put("state", "1004"); //工单转单
            ownerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_DOING);
            ownerSettledApplyV1InnerServiceSMOImpl.updateOwnerSettledApply(ownerSettledApplyPo);
        } else if ("1200".equals(reqJson.getString("auditCode"))
                || "1400".equals(reqJson.getString("auditCode"))
        ) { //退回操作
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            //reqJson.put("state", "1003"); //工单退单
            ownerSettledApplyPo.setState(OwnerSettledApplyDto.STATE_FAIT);
            ownerSettledApplyV1InnerServiceSMOImpl.updateOwnerSettledApply(ownerSettledApplyPo);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }

    }
}
