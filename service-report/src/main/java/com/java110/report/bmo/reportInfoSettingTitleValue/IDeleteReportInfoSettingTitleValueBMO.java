package com.java110.report.bmo.reportInfoSettingTitleValue;
import com.java110.po.reportInfo.ReportInfoSettingTitleValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportInfoSettingTitleValueBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param reportInfoSettingTitleValuePo
     * @return
     */
    ResponseEntity<String> delete(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo);


}
