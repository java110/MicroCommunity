package com.java110.report.bmo.reportInfoAnswer.impl;

import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.report.bmo.reportInfoAnswer.IGetReportInfoAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.reportInfoAnswer.ReportInfoAnswerDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoAnswerBMOImpl")
public class GetReportInfoAnswerBMOImpl implements IGetReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     *
     *
     * @param  reportInfoAnswerDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoAnswerDto reportInfoAnswerDto) {


        int count = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswersCount(reportInfoAnswerDto);

        List<ReportInfoAnswerDto> reportInfoAnswerDtos = null;
        if (count > 0) {
            reportInfoAnswerDtos = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswers(reportInfoAnswerDto);
        } else {
            reportInfoAnswerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoAnswerDto.getRow()), count, reportInfoAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
