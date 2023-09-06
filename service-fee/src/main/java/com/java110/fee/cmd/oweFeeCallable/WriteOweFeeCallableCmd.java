package com.java110.fee.cmd.oweFeeCallable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oweFeeCallable.OweFeeCallableDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IOweFeeCallableV1InnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.oweFeeCallable.OweFeeCallablePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 登记催缴记录
 */
@Java110Cmd(serviceCode = "oweFeeCallable.writeOweFeeCallable")
public class WriteOweFeeCallableCmd extends Cmd {

    @Autowired
    private IOweFeeCallableV1InnerServiceSMO oweFeeCallableV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋");

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        if (feeIds == null || feeIds.size() < 1) {
            throw new CmdException("未包含费用");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String staffId = context.getReqHeaders().get("user-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(staffId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不在");

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        for (int feeIndex = 0; feeIndex < feeIds.size(); feeIndex++) {
            doWriteFee(feeIds.getString(feeIndex), reqJson, userDtos.get(0));
        }


    }

    private void doWriteFee(String feeId, JSONObject reqJson, UserDto userDto) {


        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setFeeId(feeId);
        reportOweFeeDto.setCommunityId(reqJson.getString("communityId"));
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        Assert.listOnlyOne(reportOweFeeDtos, "欠费不存在");

        String ownerId = reportOweFeeDtos.get(0).getOwnerId();
        String ownerName = reportOweFeeDtos.get(0).getOwnerName();

        if(StringUtil.isEmpty(ownerId)){
            ownerId = "-1";
        }

        if(StringUtil.isEmpty(ownerName)){
            ownerName = "无业主";
        }

        //todo
        OweFeeCallablePo oweFeeCallablePo = new OweFeeCallablePo();

        oweFeeCallablePo.setAmountdOwed(reportOweFeeDtos.get(0).getAmountOwed());
        oweFeeCallablePo.setCallableWay(OweFeeCallableDto.CALLABLE_WAY_PRINT);
        oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId("11"));
        oweFeeCallablePo.setFeeId(reportOweFeeDtos.get(0).getFeeId());
        oweFeeCallablePo.setFeeName(reportOweFeeDtos.get(0).getFeeName());
        oweFeeCallablePo.setCommunityId(reqJson.getString("communityId"));
        oweFeeCallablePo.setConfigId(reportOweFeeDtos.get(0).getConfigId());
        oweFeeCallablePo.setOwnerId(ownerId);
        oweFeeCallablePo.setOwnerName(ownerName);
        oweFeeCallablePo.setPayerObjId(reportOweFeeDtos.get(0).getPayerObjId());
        oweFeeCallablePo.setPayerObjName(reportOweFeeDtos.get(0).getPayerObjName());
        oweFeeCallablePo.setPayerObjType(reportOweFeeDtos.get(0).getPayerObjType());
        oweFeeCallablePo.setRemark(reqJson.getString("remark"));
        oweFeeCallablePo.setStaffId(userDto.getUserId());
        oweFeeCallablePo.setStaffName(userDto.getName());
        oweFeeCallablePo.setState(OweFeeCallableDto.STATE_COMPLETE);
        oweFeeCallablePo.setStartTime(reportOweFeeDtos.get(0).getEndTime());
        oweFeeCallablePo.setEndTime(reportOweFeeDtos.get(0).getDeadlineTime());

        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallable(oweFeeCallablePo);

        if (flag < 1) {
            throw new CmdException("登记失败");
        }


    }
}
