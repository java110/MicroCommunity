package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStore.listAllocationStoreHisAuditOrders")
public class ListAllocationStoreHisAuditOrdersCmd extends Cmd {

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));
        auditUser.setStoreId(reqJson.getString("storeId"));
        //调拨已办(（默认只查询和当前登录用户相关并且是参与流程审批或者自己提交已经结束的审批数据）)
        long count = allocationStorehouseUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        List<AllocationStorehouseApplyDto> purchaseApplyDtos = null;

        if (count > 0) {
            purchaseApplyDtos = allocationStorehouseUserInnerServiceSMOImpl.getUserHistoryTasks(auditUser);
        } else {
            purchaseApplyDtos = new ArrayList<>();
        }

        ResponseEntity responseEntity
                = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                (int) count,
                purchaseApplyDtos);
        context.setResponseEntity(responseEntity);
    }
}
