package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;

import java.text.ParseException;

/**
 * 查询费用类统计
 */
@Java110Cmd(serviceCode = "dataReport.queryFeeDataReport")
public class QueryFeeDataReportCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束时间");
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if(!startDate.contains(":")){
            startDate += " 00:00:00";
            reqJson.put("startDate",startDate);
        }
        if(!endDate.contains(":")){
            endDate += " 23:59:59";
            reqJson.put("endDate",endDate);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        // todo 查询 实收金额
        // todo 查询 优惠金额
        // todo 查询 滞纳金
        // todo 查询 账户预存
        // todo 查询 账户扣款
        // todo 查询 临时车收入
        // todo 查询 押金退款
        // todo 查询 退款订单数
        // todo 查询 退款金额
        // todo 查询 欠费金额
        // todo 查询 充电金额


    }
}
