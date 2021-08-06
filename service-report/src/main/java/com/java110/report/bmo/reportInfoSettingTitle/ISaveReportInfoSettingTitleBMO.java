package com.java110.report.bmo.reportInfoSettingTitle;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoSettingTitleBMO {


    /**
     * 添加进出上报题目设置
     * add by wuxw
     * @param reportInfoSettingTitlePo
     * @return
     */
    ResponseEntity<String> save(ReportInfoSettingTitlePo reportInfoSettingTitlePo, JSONArray titleValues);


}
