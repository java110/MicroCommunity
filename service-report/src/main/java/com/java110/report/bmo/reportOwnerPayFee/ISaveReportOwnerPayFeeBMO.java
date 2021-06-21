package com.java110.report.bmo.reportOwnerPayFee;

import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportOwnerPayFeeBMO {


    /**
     * 添加业主缴费明细
     * add by wuxw
     * @param reportOwnerPayFeePo
     * @return
     */
    ResponseEntity<String> save(ReportOwnerPayFeePo reportOwnerPayFeePo);


}
