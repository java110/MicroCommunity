package com.java110.report.bmo.reportFeeYearCollection;

import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportFeeYearCollectionBMO {


    /**
     * 添加费用年收费
     * add by wuxw
     * @param reportFeeYearCollectionPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeYearCollectionPo reportFeeYearCollectionPo);


}
