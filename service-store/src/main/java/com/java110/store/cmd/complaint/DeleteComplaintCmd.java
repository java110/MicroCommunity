package com.java110.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.community.IComplaintV1InnerServiceSMO;
import com.java110.po.complaint.ComplaintPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "complaint.deleteComplaint")
public class DeleteComplaintCmd extends Cmd{

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "complaintId", "投诉ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ComplaintPo complaintPo = BeanConvertUtil.covertBean(reqJson, ComplaintPo.class);
        int flag = complaintV1InnerServiceSMOImpl.deleteComplaint(complaintPo);
        if (flag < 1) {
            throw new CmdException("投诉不存在");
        }


        if(StringUtil.isEmpty(complaintPo.getTaskId())){
            return ;
        }
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setTaskId(complaintPo.getTaskId());
        complaintUserInnerServiceSMOImpl.deleteTask(complaintDto);
    }
}
