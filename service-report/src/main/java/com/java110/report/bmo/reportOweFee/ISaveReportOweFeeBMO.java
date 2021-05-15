package com.java110.report.bmo.reportOweFee;

import com.java110.po.reportOweFee.ReportOweFeePo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportOweFeeBMO {


    /**
     * 添加欠费统计
     * add by wuxw
     * @param reportOweFeePo
     * @return
     */
    ResponseEntity<String> save(ReportOweFeePo reportOweFeePo);


}
