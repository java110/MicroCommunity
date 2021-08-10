package com.java110.report.bmo.reportInfoAnswerValue;

import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoAnswerValueBMO {


    /**
     * 添加批量操作日志详情
     * add by wuxw
     * @param reportInfoAnswerValuePo
     * @return
     */
    ResponseEntity<String> save(ReportInfoAnswerValuePo reportInfoAnswerValuePo);


}
