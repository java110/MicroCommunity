package com.java110.report.bmo.reportInfoAnswerValue;
import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportInfoAnswerValueBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param reportInfoAnswerValuePo
     * @return
     */
    ResponseEntity<String> delete(ReportInfoAnswerValuePo reportInfoAnswerValuePo);


}
