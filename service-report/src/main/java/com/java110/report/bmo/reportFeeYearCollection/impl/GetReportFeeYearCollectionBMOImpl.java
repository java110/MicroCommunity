package com.java110.report.bmo.reportFeeYearCollection.impl;

import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.report.bmo.reportFeeYearCollection.IGetReportFeeYearCollectionBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportFeeYearCollectionBMOImpl")
public class GetReportFeeYearCollectionBMOImpl implements IGetReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportFeeYearCollectionDto reportFeeYearCollectionDto) {


        int count = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollectionsCount(reportFeeYearCollectionDto);

        List<ReportFeeYearCollectionDto> reportFeeYearCollectionDtos = null;
        if (count > 0) {
            reportFeeYearCollectionDtos = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollections(reportFeeYearCollectionDto);
        } else {
            reportFeeYearCollectionDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeYearCollectionDto.getRow()), count, reportFeeYearCollectionDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
