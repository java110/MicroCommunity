package com.java110.report.bmo.reportFeeYearCollectionDetail;
import com.java110.po.reportFee.ReportFeeYearCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeYearCollectionDetailBMO {


    /**
     * 修改费用年收费明细
     * add by wuxw
     * @param reportFeeYearCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);


}
