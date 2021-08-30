package com.java110.oa.bmo.oaWorkflowData;

import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowDataBMO {


    /**
     * 添加OA表单审批数据
     * add by wuxw
     * @param oaWorkflowDataPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowDataPo oaWorkflowDataPo);


}
