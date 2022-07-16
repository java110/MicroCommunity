package com.java110.common.cmd.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IResourceEntryStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.resourceOrder.ApiResourceOrderDataVo;
import com.java110.vo.api.resourceOrder.ApiResourceOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "auditUser.listAuditHistoryOrders")
public class ListAuditHistoryOrdersCmd extends Cmd {

    @Autowired
    private IResourceEntryStoreInnerServiceSMO resourceEntryStoreInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));
        auditUser.setStoreId(reqJson.getString("storeId"));
        //采购已办（默认只查询和当前登录用户相关并且是参与流程审批或者自己提交已经结束的审批数据）
        long count = resourceEntryStoreInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        List<ApiResourceOrderDataVo> auditOrders = null;

        if (count > 0) {
            List<PurchaseApplyDto> purchaseApplyDtos = resourceEntryStoreInnerServiceSMOImpl.getUserHistoryTasks(auditUser);
            auditOrders = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiResourceOrderDataVo.class);
            for (ApiResourceOrderDataVo apiResourceOrderDataVo : auditOrders) {
                switch (apiResourceOrderDataVo.getState()) {
                    case "1000":
                        apiResourceOrderDataVo.setStateName("未审核");
                        break;
                    case "1001":
                        apiResourceOrderDataVo.setStateName("审核中");
                        break;
                    case "1002":
                        apiResourceOrderDataVo.setStateName("已审核");
                        break;
                }
                if (apiResourceOrderDataVo.getResOrderType().equals("10000")) {
                    apiResourceOrderDataVo.setResOrderTypeName("采购申请");
                } else {
                    apiResourceOrderDataVo.setResOrderTypeName("出库申请");
                }

            }
        } else {
            auditOrders = new ArrayList<>();
        }

        ApiResourceOrderVo apiResourceOrderVo = new ApiResourceOrderVo();

        apiResourceOrderVo.setTotal((int) count);
        apiResourceOrderVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiResourceOrderVo.setResourceOrders(auditOrders);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiResourceOrderVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
