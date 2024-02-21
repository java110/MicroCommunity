package com.java110.common.cmd.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.audit.AuditUser;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
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
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(context);

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStaffId(userId);
        complaintDto.setStoreId(reqJson.getString("storeId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setPage(reqJson.getInteger("page"));
        complaintDto.setRow(reqJson.getInteger("row"));
        complaintDto.setState(ComplaintDto.STATE_WAIT);

        long count = complaintV1InnerServiceSMOImpl.queryStaffComplaintCount(complaintDto);

        List<ComplaintDto> complaintDtos = null;

        if (count > 0) {
            complaintDtos = complaintV1InnerServiceSMOImpl.queryStaffComplaints(complaintDto);
        } else {
            complaintDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
