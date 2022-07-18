package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.intf.community.IComplaintV1InnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.po.complaint.ComplaintPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "complaint.updateComplaint")
public class UpdateComplaintCmd extends Cmd {

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择投诉类型");
        //Assert.hasKeyAndValue(reqJson, "roomId", "必填，请选择房屋编号");
        Assert.hasKeyAndValue(reqJson, "complaintName", "必填，请填写投诉人");
        Assert.hasKeyAndValue(reqJson, "tel", "必填，请填写投诉电话");
        //Assert.hasKeyAndValue(reqJson, "state", "必填，请填写投诉状态");
        Assert.hasKeyAndValue(reqJson, "context", "必填，请填写投诉内容");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {


        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(reqJson.getString("storeId"));
        complaintDto.setComplaintId(reqJson.getString("complaintId"));
        List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);

        Assert.listOnlyOne(complaintDtos, "存在多条记录，或不存在数据" + complaintDto.getComplaintId());


        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(reqJson);
        businessComplaint.put("state", complaintDtos.get(0).getState());
        businessComplaint.put("roomId", complaintDtos.get(0).getRoomId());
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(reqJson, ComplaintPo.class);
        int flag = complaintV1InnerServiceSMOImpl.updateComplaint(complaintPo);
        if(flag < 1){
            throw new CmdException("修改投诉失败");
        }
    }
}
