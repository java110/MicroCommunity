package com.java110.oa.bmo.oaWorkflowXml;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowXmlBMO {


    /**
     * 修改OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowXmlPo oaWorkflowXmlPo);


}
