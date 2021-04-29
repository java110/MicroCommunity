package com.java110.report.bmo.reportFeeMonthStatistics;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeMonthStatisticsBMO {


    /**
     * 修改费用月统计
     * add by wuxw
     * @param reportFeeMonthStatisticsPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);


}
