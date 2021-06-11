package com.java110.report.bmo.reportOwnerPayFee;
import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportOwnerPayFeeBMO {


    /**
     * 查询业主缴费明细
     * add by wuxw
     * @param  reportOwnerPayFeeDto
     * @return
     */
    ResponseEntity<String> get(ReportOwnerPayFeeDto reportOwnerPayFeeDto);


}
