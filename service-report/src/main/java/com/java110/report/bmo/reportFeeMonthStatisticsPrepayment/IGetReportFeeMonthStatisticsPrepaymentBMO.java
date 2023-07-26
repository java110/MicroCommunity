package com.java110.report.bmo.reportFeeMonthStatisticsPrepayment;

import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportFeeMonthStatisticsPrepaymentBMO {

    ResponseEntity<String> queryPayFeeDetail(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    ResponseEntity<String> queryReportCollectFees(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

}
