package com.java110.report.bmo.reportInfoSetting;

import com.java110.po.reportInfoSetting.ReportInfoSettingPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoSettingBMO {


    /**
     * 添加进出上报
     * add by wuxw
     * @param reportInfoSettingPo
     * @return
     */
    ResponseEntity<String> save(ReportInfoSettingPo reportInfoSettingPo);


}
