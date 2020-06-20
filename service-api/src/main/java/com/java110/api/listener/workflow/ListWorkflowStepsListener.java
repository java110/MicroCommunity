package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.common.IWorkflowInnerServiceSMO;
import com.java110.core.smo.common.IWorkflowStepInnerServiceSMO;
import com.java110.core.smo.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowStepDto;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import com.java110.utils.constant.ServiceCodeWorkflowStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listWorkflowStepsListener")
public class ListWorkflowStepsListener extends AbstractServiceApiListener {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

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

        Assert.hasKeyAndValue(reqJson, "flowId", "未包含流程ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");


    }

    /**
     * {"flowId":"752020061762800001","flowName":"投诉建议流程","describle":"xxxx",
     * * "steps":[{"seq":0,"staffId":"302020060983230118","staffName":"孙印玉测试","type":"1",
     * * "subStaff":[{"id":"d1aa0277-ff10-480e-855c-c90a1789909b","staffId":"302020061069500135","staffName":"天宫妹"}]},
     * * {"seq":1,"staffId":"302020042299500115","staffName":"阿光","type":"2","subStaff":[]}]}"
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     */
    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WorkflowDto workflowDto = BeanConvertUtil.covertBean(reqJson, WorkflowDto.class);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        Assert.listOnlyOne(workflowDtos,"查询条件错误");

        workflowDto = workflowDtos.get(0);

        WorkflowStepDto workflowStepDto = new WorkflowStepDto();
        workflowStepDto.setFlowId(workflowDto.getFlowId());

        List<WorkflowStepDto> workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);

        for(WorkflowStepDto tmpWorkflowStepDto : workflowStepDtos){
            WorkflowStepStaffDto workflowStepStaffDto = new WorkflowStepStaffDto();
            workflowStepStaffDto.setCommunityId(workflowDto.getCommunityId());
            workflowStepStaffDto.setStepId(tmpWorkflowStepDto.getStepId());
            List<WorkflowStepStaffDto> workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);
            tmpWorkflowStepDto.setWorkflowStepStaffs(workflowStepStaffDtos);
        }

        workflowDto.setWorkflowSteps(workflowStepDtos);

        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, workflowDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
