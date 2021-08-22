package com.java110.oa.bmo.oaWorkflowForm;

import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowFormBMO {


    /**
     * 添加OA表单
     * add by wuxw
     * @param oaWorkflowFormPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowFormPo oaWorkflowFormPo);


}
