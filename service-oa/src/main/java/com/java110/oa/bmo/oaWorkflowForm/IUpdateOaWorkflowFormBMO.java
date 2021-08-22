package com.java110.oa.bmo.oaWorkflowForm;
import com.java110.po.oaWorkflowForm.OaWorkflowFormPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowFormBMO {


    /**
     * 修改OA表单
     * add by wuxw
     * @param oaWorkflowFormPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowFormPo oaWorkflowFormPo);


}
