package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IWorkflowStepInnerServiceSMO;
import com.java110.dto.workflow.WorkflowStepDto;
import com.java110.utils.constant.ServiceCodeWorkflowStepConstant;
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
@Java110Listener("listWorkflowStepsListener")
public class ListWorkflowStepsListener extends AbstractServiceApiListener {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowStepConstant.LIST_WORKFLOWSTEPS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IWorkflowStepInnerServiceSMO getWorkflowStepInnerServiceSMOImpl() {
        return workflowStepInnerServiceSMOImpl;
    }

    public void setWorkflowStepInnerServiceSMOImpl(IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl) {
        this.workflowStepInnerServiceSMOImpl = workflowStepInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WorkflowStepDto workflowStepDto = BeanConvertUtil.covertBean(reqJson, WorkflowStepDto.class);

        int count = workflowStepInnerServiceSMOImpl.queryWorkflowStepsCount(workflowStepDto);

        List<WorkflowStepDto> workflowStepDtos = null;

        if (count > 0) {
            workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);
        } else {
            workflowStepDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workflowStepDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
