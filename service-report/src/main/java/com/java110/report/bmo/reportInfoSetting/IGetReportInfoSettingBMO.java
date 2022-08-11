package com.java110.report.bmo.reportInfoSetting;

import com.java110.dto.reportInfoSetting.ReportInfoSettingDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportInfoSettingBMO {

    /**
     * 查询进出上报
     * add by wuxw
     *
     * @param reportInfoSettingDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoSettingDto reportInfoSettingDto);

}
