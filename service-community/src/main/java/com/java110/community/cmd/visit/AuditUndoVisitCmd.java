package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.itemRelease.ItemReleaseDto;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.common.IItemReleaseV1InnerServiceSMO;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.community.IVisitSettingV1InnerServiceSMO;
import com.java110.intf.community.IVisitV1InnerServiceSMO;
import com.java110.po.itemRelease.ItemReleasePo;
import com.java110.po.owner.VisitPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 审核 放行
 */
@Java110Cmd(serviceCode = "visit.auditUndoVisit")
public class AuditUndoVisitCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Autowired
    private IVisitV1InnerServiceSMO visitV1InnerServiceSMOImpl;

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "vId", "未包含访客ID");
        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含状态说明");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        VisitDto visitDto = new VisitDto();
        visitDto.setvId(reqJson.getString("vId"));
        List<VisitDto> visitDtos = visitV1InnerServiceSMOImpl.queryVisits(visitDto);

        Assert.listOnlyOne(visitDtos, "访客不存在");

        //状态 W待审核 D 审核中 C 审核完成 D 审核失败
        VisitPo visitPo = new VisitPo();
        visitPo.setvId(visitDtos.get(0).getvId());
        reqJson.put("id",reqJson.getString("vId"));
        reqJson.put("storeId",storeId);


        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))
                || "1500".equals(reqJson.getString("auditCode"))) { //办理操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                visitPo.setState(VisitDto.STATE_C);
            } else {
                visitPo.setState(VisitDto.STATE_D);
            }
            visitV1InnerServiceSMOImpl.updateVisit(visitPo);
            //完成当前流程 插入下一处理人
        } else if ("1300".equals(reqJson.getString("auditCode"))) { //转单操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            //reqJson.put("state", "1004"); //工单转单
            visitPo.setState(VisitDto.STATE_D);
            visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        } else if ("1200".equals(reqJson.getString("auditCode"))
                || "1400".equals(reqJson.getString("auditCode"))
        ) { //退回操作
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            //reqJson.put("state", "1003"); //工单退单
            visitPo.setState(VisitDto.STATE_F);
            visitV1InnerServiceSMOImpl.updateVisit(visitPo);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }

    }
}
