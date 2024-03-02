package com.java110.store.cmd.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "purchaseApply.auditApplyOrder")
public class AuditApplyOrderCmd extends Cmd {

    @Autowired
    private IPurchaseApplyV1InnerServiceSMO purchaseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "id", "订单号不能为空");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含状态说明");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(reqJson.getString("id"));
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyV1InnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "采购申请不存在");
        //状态 W待审核 D 审核中 C 审核完成 D 审核失败
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(purchaseApplyDtos.get(0).getApplyOrderId());
        reqJson.put("id", reqJson.getString("id"));
        reqJson.put("storeId", storeId);
        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))
                || "1500".equals(reqJson.getString("auditCode"))) { //办理操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
            } else {
                purchaseApplyPo.setState(PurchaseApplyDto.STATE_DEALING);
            }
            purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);
            //完成当前流程 插入下一处理人
        } else if ("1600".equals(reqJson.getString("auditCode"))) { //重新提交操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
            } else {
                purchaseApplyPo.setState(PurchaseApplyDto.STATE_WAIT_DEAL);
            }
            purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);
            //完成当前流程 插入下一处理人
        } else if ("1300".equals(reqJson.getString("auditCode"))) { //转单操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            //reqJson.put("state", "1004"); //工单转单
            purchaseApplyPo.setState(PurchaseApplyDto.STATE_DEALING);
            purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);
        } else if ("1200".equals(reqJson.getString("auditCode"))
                || "1400".equals(reqJson.getString("auditCode"))
        ) { //退回操作
            reqJson.put("startUserId", purchaseApplyDtos.get(0).getCreateUserId());
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            //reqJson.put("state", "1003"); //工单退单
            purchaseApplyPo.setState(PurchaseApplyDto.STATE_NOT_PASS);
            purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }
    }
}
