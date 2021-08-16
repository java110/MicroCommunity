package com.java110.oa.bmo.oaWorkflow;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowBMO {


    /**
     * 修改OA工作流
     * add by wuxw
     * @param oaWorkflowPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowPo oaWorkflowPo);


}
