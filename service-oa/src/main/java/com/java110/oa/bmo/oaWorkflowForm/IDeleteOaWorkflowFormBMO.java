package com.java110.oa.bmo.oaWorkflowForm;
import com.java110.po.oaWorkflow.OaWorkflowFormPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowFormBMO {


    /**
     * 修改OA表单
     * add by wuxw
     * @param oaWorkflowFormPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowFormPo oaWorkflowFormPo);


}
