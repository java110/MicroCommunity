package com.java110.report.bmo.reportOweFee;
import com.java110.po.reportOweFee.ReportOweFeePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportOweFeeBMO {


    /**
     * 修改欠费统计
     * add by wuxw
     * @param reportOweFeePo
     * @return
     */
    ResponseEntity<String> update(ReportOweFeePo reportOweFeePo);


}
