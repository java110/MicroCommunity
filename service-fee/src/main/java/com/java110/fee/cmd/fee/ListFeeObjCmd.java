package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.bmo.IQueryOweFee;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "/feeApi/listFeeObj")
public class ListFeeObjCmd extends Cmd {

    @Autowired
    private IQueryOweFee queryOweFeeImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含费用信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        if (reqJson.containsKey("cycle") && !StringUtil.isEmpty(reqJson.getString("cycle"))) {
            feeDto.setCycle(reqJson.getString("cycle"));
        }
        if (reqJson.containsKey("custEndTime") && !StringUtil.isEmpty(reqJson.getString("custEndTime"))) {
            feeDto.setCustEndTime(reqJson.getString("custEndTime"));
        }
        ResponseEntity<String> result = queryOweFeeImpl.listFeeObj(feeDto);
        context.setResponseEntity(result);
    }
}
