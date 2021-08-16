package com.java110.oa.bmo.oaWorkflow;

import com.java110.dto.oaWorkflow.OaWorkflowDto;
import org.springframework.http.ResponseEntity;

public interface IGetOaWorkflowBMO {


    /**
     * 查询OA工作流
     * add by wuxw
     *
     * @param oaWorkflowDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowDto oaWorkflowDto);


}
