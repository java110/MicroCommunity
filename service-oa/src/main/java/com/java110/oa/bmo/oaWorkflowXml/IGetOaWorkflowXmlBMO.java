package com.java110.oa.bmo.oaWorkflowXml;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import org.springframework.http.ResponseEntity;
public interface IGetOaWorkflowXmlBMO {


    /**
     * 查询OA流程图
     * add by wuxw
     * @param  oaWorkflowXmlDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowXmlDto oaWorkflowXmlDto);


}
