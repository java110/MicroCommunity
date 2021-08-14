package com.java110.report.bmo.reportFeeMonthCollectionDetail.impl;

import com.java110.dto.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailDto;
import com.java110.intf.report.IReportFeeMonthCollectionDetailInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.IGetReportFeeMonthCollectionDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportFeeMonthCollectionDetailBMOImpl")
public class GetReportFeeMonthCollectionDetailBMOImpl implements IGetReportFeeMonthCollectionDetailBMO {

    @Autowired
    private IReportFeeMonthCollectionDetailInnerServiceSMO reportFeeMonthCollectionDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportFeeMonthCollectionDetailDto reportFeeMonthCollectionDetailDto) {


        int count = reportFeeMonthCollectionDetailInnerServiceSMOImpl.queryReportFeeMonthCollectionDetailsCount(reportFeeMonthCollectionDetailDto);

        List<ReportFeeMonthCollectionDetailDto> reportFeeMonthCollectionDetailDtos = null;
        if (count > 0) {
            reportFeeMonthCollectionDetailDtos = reportFeeMonthCollectionDetailInnerServiceSMOImpl.queryReportFeeMonthCollectionDetails(reportFeeMonthCollectionDetailDto);
        } else {
            reportFeeMonthCollectionDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthCollectionDetailDto.getRow()), count, reportFeeMonthCollectionDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
