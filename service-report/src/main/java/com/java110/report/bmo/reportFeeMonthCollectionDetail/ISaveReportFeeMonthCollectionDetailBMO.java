package com.java110.report.bmo.reportFeeMonthCollectionDetail;

import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportFeeMonthCollectionDetailBMO {


    /**
     * 添加月缴费表
     * add by wuxw
     * @param reportFeeMonthCollectionDetailPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo);


}
