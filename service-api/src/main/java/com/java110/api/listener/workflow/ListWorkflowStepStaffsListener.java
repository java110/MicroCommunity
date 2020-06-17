package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import com.java110.utils.constant.ServiceCodeWorkflowStepStaffConstant;
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
@Java110Listener("listWorkflowStepStaffsListener")
public class ListWorkflowStepStaffsListener extends AbstractServiceApiListener {

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowStepStaffConstant.LIST_WORKFLOWSTEPSTAFFS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IWorkflowStepStaffInnerServiceSMO getWorkflowStepStaffInnerServiceSMOImpl() {
        return workflowStepStaffInnerServiceSMOImpl;
    }

    public void setWorkflowStepStaffInnerServiceSMOImpl(IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl) {
        this.workflowStepStaffInnerServiceSMOImpl = workflowStepStaffInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WorkflowStepStaffDto workflowStepStaffDto = BeanConvertUtil.covertBean(reqJson, WorkflowStepStaffDto.class);

        int count = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffsCount(workflowStepStaffDto);

        List<WorkflowStepStaffDto> workflowStepStaffDtos = null;

        if (count > 0) {
            workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);
        } else {
            workflowStepStaffDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workflowStepStaffDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
