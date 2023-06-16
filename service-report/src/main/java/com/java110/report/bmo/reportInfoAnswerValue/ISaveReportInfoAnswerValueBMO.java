package com.java110.report.bmo.reportInfoAnswerValue;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoAnswerValueBMO {


    /**
     * 添加批量操作日志详情
     * add by wuxw
     * @param
     * @return
     */
    ResponseEntity<String> save(JSONObject reqJson, String userId);


}
