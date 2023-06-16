package com.java110.report.bmo.reportInfoAnswer;
import com.java110.dto.reportInfo.ReportInfoAnswerDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportInfoAnswerBMO {


    /**
     * 查询批量操作日志详情
     * add by wuxw
     * @param  reportInfoAnswerDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoAnswerDto reportInfoAnswerDto);


}
