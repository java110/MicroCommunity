package com.java110.report.bmo.reportFeeYearCollectionDetail;
import com.java110.dto.reportFee.ReportFeeYearCollectionDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportFeeYearCollectionDetailBMO {


    /**
     * 查询费用年收费明细
     * add by wuxw
     * @param  reportFeeYearCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto);


}
