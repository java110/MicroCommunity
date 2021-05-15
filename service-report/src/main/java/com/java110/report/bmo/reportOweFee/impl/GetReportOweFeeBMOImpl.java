package com.java110.report.bmo.reportOweFee.impl;

import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.report.bmo.reportOweFee.IGetReportOweFeeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportOweFeeBMOImpl")
public class GetReportOweFeeBMOImpl implements IGetReportOweFeeBMO {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    /**
     * @param reportOweFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportOweFeeDto reportOweFeeDto) {


        int count = reportOweFeeInnerServiceSMOImpl.queryReportOweFeesCount(reportOweFeeDto);

        List<ReportOweFeeDto> reportOweFeeDtos = null;
        if (count > 0) {
            reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportOweFees(reportOweFeeDto);
        } else {
            reportOweFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportOweFeeDto.getRow()), count, reportOweFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
