package com.java110.common.cmd.itemRelease;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.itemRelease.ItemReleaseDto;
import com.java110.intf.common.IItemReleaseV1InnerServiceSMO;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.po.itemRelease.ItemReleasePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 审核 放行
 */
@Java110Cmd(serviceCode = "itemRelease.auditUndoItemRelease")
public class AuditUndoItemReleaseCmd extends Cmd {

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Autowired
    private IItemReleaseV1InnerServiceSMO itemReleaseV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务");
        Assert.hasKeyAndValue(reqJson, "irId", "未包含放行");
        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程");
        Assert.hasKeyAndValue(reqJson, "auditCode", "未包含状态");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含状态说明");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");

        ItemReleaseDto itemReleaseDto = new ItemReleaseDto();
        itemReleaseDto.setIrId(reqJson.getString("irId"));
        List<ItemReleaseDto> itemReleaseDtos = itemReleaseV1InnerServiceSMOImpl.queryItemReleases(itemReleaseDto);

        Assert.listOnlyOne(itemReleaseDtos, "物品放行不存在");

        //状态 W待审核 D 审核中 C 审核完成 D 审核失败
        ItemReleasePo itemReleasePo = new ItemReleasePo();
        itemReleasePo.setIrId(itemReleaseDtos.get(0).getIrId());
        reqJson.put("id",reqJson.getString("irId"));
        reqJson.put("storeId",storeId);


        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))
                || "1500".equals(reqJson.getString("auditCode"))) { //办理操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                itemReleasePo.setState(ItemReleaseDto.STATE_COMPLETE);
            } else {
                itemReleasePo.setState(ItemReleaseDto.STATE_DOING);
            }
            itemReleaseV1InnerServiceSMOImpl.updateItemRelease(itemReleasePo);
            //完成当前流程 插入下一处理人
        } else if ("1300".equals(reqJson.getString("auditCode"))) { //转单操作
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            //reqJson.put("state", "1004"); //工单转单
            itemReleasePo.setState(ItemReleaseDto.STATE_DOING);
            itemReleaseV1InnerServiceSMOImpl.updateItemRelease(itemReleasePo);
        } else if ("1200".equals(reqJson.getString("auditCode"))
                || "1400".equals(reqJson.getString("auditCode"))
        ) { //退回操作
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            //reqJson.put("state", "1003"); //工单退单
            itemReleasePo.setState(ItemReleaseDto.STATE_FAIT);
            itemReleaseV1InnerServiceSMOImpl.updateItemRelease(itemReleasePo);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }

    }
}
