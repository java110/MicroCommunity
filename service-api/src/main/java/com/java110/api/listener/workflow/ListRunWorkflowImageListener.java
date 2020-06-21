package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IWorkflowInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeWorkflowConstant;
import com.java110.utils.exception.Result;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 查询小区侦听类
 */
@Java110Listener("listRunWorkflowImageListener")
public class ListRunWorkflowImageListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowConstant.LIST_RUN_WORKFLOW_IMAGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    public IWorkflowInnerServiceSMO getWorkflowInnerServiceSMOImpl() {
        return workflowInnerServiceSMOImpl;
    }

    public void setWorkflowInnerServiceSMOImpl(IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl) {
        this.workflowInnerServiceSMOImpl = workflowInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "businessKey", "请求报文中请包含业主Key");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String image = workflowInnerServiceSMOImpl.getRunWorkflowImage(reqJson.getString("businessKey"));
        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, image);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);


    }
}
