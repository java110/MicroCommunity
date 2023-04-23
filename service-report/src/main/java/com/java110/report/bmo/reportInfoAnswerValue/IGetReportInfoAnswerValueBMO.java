package com.java110.report.bmo.reportInfoAnswerValue;

import com.java110.dto.reportInfoAnswer.ReportInfoAnswerValueDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportInfoAnswerValueBMO {

    /**
     * 查询批量操作日志详情
     * add by wuxw
     *
     * @param reportInfoAnswerValueDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoAnswerValueDto reportInfoAnswerValueDto);

}
