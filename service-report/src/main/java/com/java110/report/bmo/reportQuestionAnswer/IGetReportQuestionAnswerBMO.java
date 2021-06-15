package com.java110.report.bmo.reportQuestionAnswer;
import com.java110.dto.reportOweFee.ReportOweFeeDto;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportQuestionAnswerBMO {


    /**
     * 查询投票明细
     * add by wuxw
     * @param  userQuestionAnswerValueDto
     * @return
     */
    ResponseEntity<String> get(UserQuestionAnswerValueDto userQuestionAnswerValueDto);

}
