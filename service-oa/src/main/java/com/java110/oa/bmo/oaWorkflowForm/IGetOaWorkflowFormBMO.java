package com.java110.oa.bmo.oaWorkflowForm;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IGetOaWorkflowFormBMO {


    /**
     * 查询OA表单
     * add by wuxw
     * @param  oaWorkflowFormDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowFormDto oaWorkflowFormDto);


    ResponseEntity<String> queryOaWorkflowFormData(Map paramIn);

    /**
     * 保存表单数据
     * @param reqJson
     * @return
     */
    ResponseEntity<String> saveOaWorkflowFormData(JSONObject reqJson);
}
