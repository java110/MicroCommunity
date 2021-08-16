package com.java110.oa.bmo.oaWorkflow;

import com.java110.po.oaWorkflow.OaWorkflowPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowBMO {


    /**
     * 添加OA工作流
     * add by wuxw
     * @param oaWorkflowPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowPo oaWorkflowPo);


}
