package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.report.statistics.IOrderStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 查询费用类统计
 */
@Java110Cmd(serviceCode = "dataReport.queryFeeDataReport")
public class QueryFeeDataReportCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IOrderStatistics orderStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束时间");
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        // todo 查询 实收金额
        double receivedFee = feeStatisticsImpl.getReceivedFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","实收金额");
        data.put("value", receivedFee);
        datas.add(data);

        // todo 查询 欠费金额
        double oweFee = feeStatisticsImpl.getOweFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","欠费金额");
        data.put("value", oweFee);
        datas.add(data);

        // todo 查询 优惠金额
        double discountFee = feeStatisticsImpl.getDiscountFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","优惠金额");
        data.put("value", discountFee);
        datas.add(data);

        // todo 查询 滞纳金
        double lateFee = feeStatisticsImpl.getLateFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","滞纳金");
        data.put("value", lateFee);
        datas.add(data);

        // todo 查询 账户预存
        double prestoreAccount = feeStatisticsImpl.getPrestoreAccount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","账户预存");
        data.put("value", prestoreAccount);
        datas.add(data);

        // todo 查询 账户扣款
        double withholdAccount = feeStatisticsImpl.getWithholdAccount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","账户扣款");
        data.put("value", withholdAccount);
        datas.add(data);

        // todo 查询 临时车收入
        double tempCarFee = feeStatisticsImpl.getTempCarFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","临时车收入");
        data.put("value", tempCarFee);
        datas.add(data);

        // todo 查询 押金退款
        double refundDeposit = feeStatisticsImpl.geRefundDeposit(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","押金退款");
        data.put("value", refundDeposit);
        datas.add(data);

        // todo 查询 退款订单数
        double refundOrderCount = feeStatisticsImpl.geRefundOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","退款订单数");
        data.put("value", refundOrderCount);
        datas.add(data);
        // todo 查询 退款金额
        double refundFee = feeStatisticsImpl.geRefundFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","退款金额");
        data.put("value", refundFee);
        datas.add(data);



        // todo 查询 充电金额
        double chargeFee = feeStatisticsImpl.getChargeFee(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","充电金额");
        data.put("value", chargeFee);
        datas.add(data);

        // todo 查询 月卡金额
        double chargeMonthOrderMoney = orderStatisticsImpl.getChargeMonthOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","月卡实收");
        data.put("value", chargeMonthOrderMoney);
        datas.add(data);


        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
