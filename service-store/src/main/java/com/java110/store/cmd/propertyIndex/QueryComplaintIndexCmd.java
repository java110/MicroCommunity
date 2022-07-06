package com.java110.store.cmd.propertyIndex;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "propertyIndex.queryComplaintIndex")
public class QueryComplaintIndexCmd extends Cmd {

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramOut = new JSONObject();

        // 全部投诉
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        int allCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("allComplaintCount", allCount);

        // 待处理
        complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setState(complaintDto.STATE_WAIT);
        int waitCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("waitComplaintCount", waitCount);


        //已完成
        complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setState(complaintDto.STATE_FINISH);
        int finishCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("finishComplaintCount", finishCount);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
