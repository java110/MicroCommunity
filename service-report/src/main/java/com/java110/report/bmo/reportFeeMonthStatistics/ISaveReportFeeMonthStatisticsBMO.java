package com.java110.report.bmo.reportFeeMonthStatistics;

import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import org.springframework.http.ResponseEntity;

public interface ISaveReportFeeMonthStatisticsBMO {


    /**
     * 添加费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);


}
