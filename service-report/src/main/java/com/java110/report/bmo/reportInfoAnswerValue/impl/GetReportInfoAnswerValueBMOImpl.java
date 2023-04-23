package com.java110.report.bmo.reportInfoAnswerValue.impl;

import com.java110.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.java110.report.bmo.reportInfoAnswerValue.IGetReportInfoAnswerValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerValueDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoAnswerValueBMOImpl")
public class GetReportInfoAnswerValueBMOImpl implements IGetReportInfoAnswerValueBMO {

    @Autowired
    private IReportInfoAnswerValueInnerServiceSMO reportInfoAnswerValueInnerServiceSMOImpl;

    /**
     * @param reportInfoAnswerValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoAnswerValueDto reportInfoAnswerValueDto) {

        int count = reportInfoAnswerValueInnerServiceSMOImpl.queryReportInfoAnswerValuesCount(reportInfoAnswerValueDto);

        List<ReportInfoAnswerValueDto> reportInfoAnswerValueDtos = null;
        if (count > 0) {
            reportInfoAnswerValueDtos = reportInfoAnswerValueInnerServiceSMOImpl.queryReportInfoAnswerValues(reportInfoAnswerValueDto);
        } else {
            reportInfoAnswerValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoAnswerValueDto.getRow()), count, reportInfoAnswerValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
