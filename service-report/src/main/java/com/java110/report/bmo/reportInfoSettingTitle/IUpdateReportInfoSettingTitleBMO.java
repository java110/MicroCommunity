package com.java110.report.bmo.reportInfoSettingTitle;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoSettingTitleBMO {


    /**
     * 修改进出上报题目设置
     * add by wuxw
     * @param reportInfoSettingTitlePo
     * @return
     */
    ResponseEntity<String> update(ReportInfoSettingTitlePo reportInfoSettingTitlePo);


}
