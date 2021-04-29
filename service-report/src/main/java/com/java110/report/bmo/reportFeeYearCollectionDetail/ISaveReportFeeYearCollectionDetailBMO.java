package com.java110.report.bmo.reportFeeYearCollectionDetail;

import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportFeeYearCollectionDetailBMO {


    /**
     * 添加费用年收费明细
     * add by wuxw
     * @param reportFeeYearCollectionDetailPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);


}
