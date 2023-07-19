package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/queryPayFeeDetailInnerServiceSMOApi")
public interface IQueryPayFeeDetailInnerServiceSMO {

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    ResultVo query(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryPrepayment", method = RequestMethod.POST)
    ResultVo queryPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    @RequestMapping(value = "/queryReportCollectFees", method = RequestMethod.POST)
    ResultVo queryReportCollectFees(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);
}
