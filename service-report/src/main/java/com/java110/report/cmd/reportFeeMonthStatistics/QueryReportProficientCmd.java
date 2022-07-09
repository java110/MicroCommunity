package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 查询报表专家 图
 */
@Java110Cmd(serviceCode = "/reportFeeMonthStatistics/queryReportProficient")
public class QueryReportProficientCmd extends Cmd {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(reqJson.getString("communityId"));

        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear()+"");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth()+"");
        JSONObject result = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportProficientCount(reportFeeMonthStatisticsDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(result.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
