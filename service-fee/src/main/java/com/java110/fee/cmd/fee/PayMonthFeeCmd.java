package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;

import java.text.ParseException;

/**
 * 按月交费
 */
@Java110Cmd(serviceCode = "fee.payMonthFee")
public class PayMonthFeeCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "primeRate", "未包含支付方式");
        if (!reqJson.containsKey("selectMonthIds")) {
            throw new CmdException("未包含缴费月份");
        }

        JSONArray selectMonthIds = reqJson.getJSONArray("selectMonthIds");
        if (selectMonthIds == null || selectMonthIds.size() < 1) {
            throw new CmdException("未包含缴费月份");
        }

        // todo 检查是否跳月了

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
