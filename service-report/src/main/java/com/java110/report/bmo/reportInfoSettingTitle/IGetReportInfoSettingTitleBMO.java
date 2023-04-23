package com.java110.report.bmo.reportInfoSettingTitle;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportInfoSettingTitleBMO {


    /**
     * 查询进出上报题目设置
     * add by wuxw
     * @param  reportInfoSettingTitleDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoSettingTitleDto reportInfoSettingTitleDto);


}
