package com.java110.report.bmo.reportFeeMonthCollectionDetail;
import com.java110.dto.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportFeeMonthCollectionDetailBMO {


    /**
     * 查询月缴费表
     * add by wuxw
     * @param  reportFeeMonthCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto);


}
