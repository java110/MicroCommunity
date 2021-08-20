package com.java110.oa.bmo.oaWorkflowXml;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowXmlBMO {


    /**
     * 修改OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowXmlPo oaWorkflowXmlPo);


}
