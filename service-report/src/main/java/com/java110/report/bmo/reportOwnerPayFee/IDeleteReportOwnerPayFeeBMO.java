package com.java110.report.bmo.reportOwnerPayFee;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportOwnerPayFeeBMO {


    /**
     * 修改业主缴费明细
     * add by wuxw
     * @param reportOwnerPayFeePo
     * @return
     */
    ResponseEntity<String> delete(ReportOwnerPayFeePo reportOwnerPayFeePo);


}
