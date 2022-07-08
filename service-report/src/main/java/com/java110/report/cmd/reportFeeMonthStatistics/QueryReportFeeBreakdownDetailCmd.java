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
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "/reportFeeMonthStatistics/queryReportFeeBreakdownDetail")
public class QueryReportFeeBreakdownDetailCmd extends Cmd {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未传入小区信息");
        Assert.hasKeyAndValue(reqJson, "configId", "未传入费用项");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsDto.class);
        if(StringUtil.isEmpty(reportFeeMonthStatisticsDto.getYearMonth())){
            reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear()+"");
            reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth()+"");
        }
        if (!StringUtil.isEmpty(reportFeeMonthStatisticsDto.getStartTime())) {
            reportFeeMonthStatisticsDto.setStartTime(reportFeeMonthStatisticsDto.getStartTime() + " 00:00:00");
        }
        if (!StringUtil.isEmpty(reportFeeMonthStatisticsDto.getEndTime())) {
            reportFeeMonthStatisticsDto.setEndTime(reportFeeMonthStatisticsDto.getEndTime() + " 23:59:59");
        }

        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdownDetailCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdownDetail(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);

    }
}
