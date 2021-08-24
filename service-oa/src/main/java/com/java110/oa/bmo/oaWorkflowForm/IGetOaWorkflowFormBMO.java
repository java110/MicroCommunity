package com.java110.oa.bmo.oaWorkflowForm;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import org.springframework.http.ResponseEntity;
public interface IGetOaWorkflowFormBMO {


    /**
     * 查询OA表单
     * add by wuxw
     * @param  oaWorkflowFormDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowFormDto oaWorkflowFormDto);


    ResponseEntity<String> queryOaWorkflowFormData(OaWorkflowFormDto oaWorkflowFormDto);
}
