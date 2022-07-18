package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.community.IComplaintV1InnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.po.complaint.ComplaintPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "complaint.auditComplaint")
public class AuditComplaintCmd extends Cmd {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;


    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写批注");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setComplaintId(reqJson.getString("complaintId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));

        List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);
        Assert.listOnlyOne(complaintDtos, "未存在或存在多条投诉单");

        complaintDto = complaintDtos.get(0);
        complaintDto.setTaskId(reqJson.getString("taskId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setStoreId(reqJson.getString("storeId"));
        complaintDto.setAuditCode(reqJson.getString("state"));
        complaintDto.setAuditMessage(reqJson.getString("remark"));
        complaintDto.setCurrentUserId(reqJson.getString("userId"));

        boolean isLastTask = complaintUserInnerServiceSMOImpl.completeTask(complaintDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        if (isLastTask) {
            complaintDto = new ComplaintDto();
            complaintDto.setStoreId(reqJson.getString("storeId"));
            complaintDto.setCommunityId(reqJson.getString("communityId"));
            complaintDto.setComplaintId(reqJson.getString("complaintId"));
            complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);

            Assert.listOnlyOne(complaintDtos, "存在多条记录，或不存在数据" + complaintDto.getComplaintId());

            JSONObject businessComplaint = new JSONObject();
            businessComplaint.putAll(BeanConvertUtil.beanCovertMap(complaintDtos.get(0)));
            businessComplaint.put("state", "10002");
            ComplaintPo complaintPo = BeanConvertUtil.covertBean(businessComplaint, ComplaintPo.class);
            int flag = complaintV1InnerServiceSMOImpl.updateComplaint(complaintPo);
            if (flag < 1) {
                throw new CmdException("投诉不存在");
            }
        }
        context.setResponseEntity(responseEntity);
    }
}
