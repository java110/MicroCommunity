package com.java110.oa.bmo.oaWorkflow;

import com.java110.po.oaWorkflow.OaWorkflowPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowBMO {


    /**
     * 修改OA工作流
     * add by wuxw
     *
     * @param oaWorkflowPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowPo oaWorkflowPo);


}
