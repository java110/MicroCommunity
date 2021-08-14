package com.java110.report.bmo.reportFeeMonthCollectionDetail;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeMonthCollectionDetailBMO {


    /**
     * 修改月缴费表
     * add by wuxw
     * @param reportFeeMonthCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo);


}
