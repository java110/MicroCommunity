package com.java110.report.bmo.reportFeeYearCollectionDetail.impl;

import com.java110.dto.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailDto;
import com.java110.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.java110.report.bmo.reportFeeYearCollectionDetail.IGetReportFeeYearCollectionDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportFeeYearCollectionDetailBMOImpl")
public class GetReportFeeYearCollectionDetailBMOImpl implements IGetReportFeeYearCollectionDetailBMO {

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto) {


        int count = reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetailsCount(reportFeeYearCollectionDetailDto);

        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetailDtos = null;
        if (count > 0) {
            reportFeeYearCollectionDetailDtos = reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);
        } else {
            reportFeeYearCollectionDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeYearCollectionDetailDto.getRow()), count, reportFeeYearCollectionDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
