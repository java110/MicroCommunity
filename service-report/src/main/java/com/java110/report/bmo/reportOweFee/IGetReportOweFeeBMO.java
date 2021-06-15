package com.java110.report.bmo.reportOweFee;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportOweFeeBMO {


    /**
     * 查询欠费统计
     * add by wuxw
     * @param  reportOweFeeDto
     * @return
     */
    ResponseEntity<String> get(ReportOweFeeDto reportOweFeeDto);


    ResponseEntity<String> getAllFees(ReportOweFeeDto reportOweFeeDto);
}
