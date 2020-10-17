package com.java110.report.bmo.reportFeeMonthStatistics;

import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportFeeMonthStatisticsBMO {


    /**
     * 查询费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    /**
     * 查询费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    ResponseEntity<String> queryReportFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    ResponseEntity<String> queryReportFloorUnitFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);


    ResponseEntity<String> queryFeeBreakdown(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

}
