package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.complaintEvent.ComplaintEventDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.store.IComplaintEventV1InnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.complaint.ComplaintPo;
import com.java110.po.complaintEvent.ComplaintEventPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.GenerateCodeException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "complaint.auditComplaint")
public class AuditComplaintCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Autowired
    private IComplaintEventV1InnerServiceSMO complaintEventV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写批注");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setComplaintId(reqJson.getString("complaintId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));

        List<ComplaintDto> complaintDtos = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);
        Assert.listOnlyOne(complaintDtos, "未存在或存在多条投诉单");

        // todo 修改投诉状态
        ComplaintPo complaintPo = new ComplaintPo();
        complaintPo.setComplaintId(complaintDtos.get(0).getComplaintId());
        complaintPo.setState(ComplaintDto.STATE_FINISH);
        complaintV1InnerServiceSMOImpl.updateComplaint(complaintPo);

        ComplaintEventPo complaintEventPo = new ComplaintEventPo();
        complaintEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        complaintEventPo.setCreateUserId(userDtos.get(0).getUserId());
        complaintEventPo.setCreateUserName(userDtos.get(0).getName());
        complaintEventPo.setComplaintId(complaintDtos.get(0).getComplaintId());
        complaintEventPo.setRemark(reqJson.getString("context"));

        complaintEventPo.setEventType(ComplaintEventDto.EVENT_TYPE_DO);
        complaintEventPo.setCommunityId(complaintDtos.get(0).getCommunityId());

        complaintEventV1InnerServiceSMOImpl.saveComplaintEvent(complaintEventPo);

        context.setResponseEntity(ResultVo.success());
    }
}
