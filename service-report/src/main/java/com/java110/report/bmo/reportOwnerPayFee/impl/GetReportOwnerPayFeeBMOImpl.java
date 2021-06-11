package com.java110.report.bmo.reportOwnerPayFee.impl;

import com.java110.dto.reportOwnerPayFee.ReportOwnerPayFeeDto;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.report.bmo.reportOwnerPayFee.IGetReportOwnerPayFeeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportOwnerPayFeeBMOImpl")
public class GetReportOwnerPayFeeBMOImpl implements IGetReportOwnerPayFeeBMO {

    @Autowired
    private IReportOwnerPayFeeInnerServiceSMO reportOwnerPayFeeInnerServiceSMOImpl;

    /**
     * @param reportOwnerPayFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportOwnerPayFeeDto reportOwnerPayFeeDto) {


        int count = reportOwnerPayFeeInnerServiceSMOImpl.queryReportOwnerPayFeesCount(reportOwnerPayFeeDto);

        List<ReportOwnerPayFeeDto> reportOwnerPayFeeDtos = null;
        if (count > 0) {
            reportOwnerPayFeeDtos = reportOwnerPayFeeInnerServiceSMOImpl.queryReportOwnerPayFees(reportOwnerPayFeeDto);
        } else {
            reportOwnerPayFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportOwnerPayFeeDto.getRow()), count, reportOwnerPayFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
