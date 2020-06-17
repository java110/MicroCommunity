package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IWorkflowInnerServiceSMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.utils.constant.ServiceCodeWorkflowConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listWorkflowsListener")
public class ListWorkflowsListener extends AbstractServiceApiListener {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowConstant.LIST_WORKFLOWS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IWorkflowInnerServiceSMO getWorkflowInnerServiceSMOImpl() {
        return workflowInnerServiceSMOImpl;
    }

    public void setWorkflowInnerServiceSMOImpl(IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl) {
        this.workflowInnerServiceSMOImpl = workflowInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);

        int count = workflowInnerServiceSMOImpl.queryWorkflowsCount(workflowDto);

        List<WorkflowDto> workflowDtos = null;

        if (count > 0) {
            workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);
        } else {
            workflowDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workflowDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
