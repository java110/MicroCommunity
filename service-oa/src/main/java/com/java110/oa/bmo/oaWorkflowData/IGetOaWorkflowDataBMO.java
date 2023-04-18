package com.java110.oa.bmo.oaWorkflowData;
import com.java110.dto.oaWorkflow.OaWorkflowDataDto;
import org.springframework.http.ResponseEntity;
public interface IGetOaWorkflowDataBMO {


    /**
     * 查询OA表单审批数据
     * add by wuxw
     * @param  oaWorkflowDataDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowDataDto oaWorkflowDataDto);


}
