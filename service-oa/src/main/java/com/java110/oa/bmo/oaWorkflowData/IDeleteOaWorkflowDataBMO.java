package com.java110.oa.bmo.oaWorkflowData;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowDataBMO {


    /**
     * 修改OA表单审批数据
     * add by wuxw
     * @param oaWorkflowDataPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowDataPo oaWorkflowDataPo);


}
