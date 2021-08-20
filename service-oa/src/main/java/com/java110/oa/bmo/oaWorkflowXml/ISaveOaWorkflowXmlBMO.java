package com.java110.oa.bmo.oaWorkflowXml;

import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowXmlBMO {


    /**
     * 添加OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowXmlPo oaWorkflowXmlPo);


}
