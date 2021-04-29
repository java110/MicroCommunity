package com.java110.report.bmo.reportFeeYearCollection;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportFeeYearCollectionBMO {


    /**
     * 查询费用年收费
     * add by wuxw
     * @param  reportFeeYearCollectionDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeYearCollectionDto reportFeeYearCollectionDto);


}
