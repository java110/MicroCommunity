package com.java110.report.bmo.reportFeeMonthStatisticsPrepayment.impl;

import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatisticsPrepayment.IGetReportFeeMonthStatisticsPrepaymentBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("getReportFeeMonthStatisticsPrepaymentBMOImpl")
public class GetReportFeeMonthStatisticsPrepaymentBMOImpl implements IGetReportFeeMonthStatisticsPrepaymentBMO {

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentInnerServiceSMO reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl;

    @Autowired
    private IQueryPayFeeDetailInnerServiceSMO queryPayFeeDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {

        int count = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepaymentsCount(reportFeeMonthStatisticsPrepaymentDto);

        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentDto);
        } else {
            reportFeeMonthStatisticsPrepaymentDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsPrepaymentDto.getRow()), count, reportFeeMonthStatisticsPrepaymentDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 前台查询分页查询
     *
     * @param reportFeeMonthStatisticsPrepaymentDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryPayFeeDetail(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryPrepayment(reportFeeMonthStatisticsPrepaymentDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryReportCollectFees(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryReportCollectFees(reportFeeMonthStatisticsPrepaymentDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }
}
