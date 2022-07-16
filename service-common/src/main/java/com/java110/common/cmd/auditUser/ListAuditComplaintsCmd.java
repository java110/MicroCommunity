package com.java110.common.cmd.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.complaint.ApiComplaintDataVo;
import com.java110.vo.api.complaint.ApiComplaintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "auditUser.listAuditComplaints")
public class ListAuditComplaintsCmd extends Cmd {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setStoreId(reqJson.getString("storeId"));
        auditUser.setCommunityId(reqJson.getString("communityId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));


        long count = complaintUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        List<ApiComplaintDataVo> auditComplaints = null;

        if (count > 0) {
            auditComplaints = BeanConvertUtil.covertBeanList(complaintUserInnerServiceSMOImpl.getUserTasks(auditUser), ApiComplaintDataVo.class);
        } else {
            auditComplaints = new ArrayList<>();
        }

        ApiComplaintVo apiComplaintVo = new ApiComplaintVo();

        apiComplaintVo.setTotal((int) count);
        apiComplaintVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiComplaintVo.setComplaints(auditComplaints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiComplaintVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
