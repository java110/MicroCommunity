package com.java110.report.bmo.reportFeeYearCollection;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportFeeYearCollectionBMO {


    /**
     * 修改费用年收费
     * add by wuxw
     * @param reportFeeYearCollectionPo
     * @return
     */
    ResponseEntity<String> update(ReportFeeYearCollectionPo reportFeeYearCollectionPo);


}
