package com.java110.api.listener.workflow;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.workflow.IWorkflowBMO;
import com.java110.api.bmo.workflow.IWorkflowStepStaffBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.common.IWorkflowInnerServiceSMO;
import com.java110.core.smo.common.IWorkflowStepInnerServiceSMO;
import com.java110.core.smo.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowStepDto;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import com.java110.po.workflow.WorkflowPo;
import com.java110.po.workflow.WorkflowStepPo;
import com.java110.po.workflow.WorkflowStepStaffPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeWorkflowConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存工作流侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateWorkflowListener")
public class UpdateWorkflowListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IWorkflowBMO workflowBMOImpl;

    @Autowired
    private IWorkflowStepStaffBMO workflowStepStaffBMO;

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");
        Assert.hasKeyAndValue(reqJson, "flowName", "请求报文中未包含flowName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");

        if (!reqJson.containsKey("steps")) {
            Assert.hasKeyAndValue(reqJson, "steps", "请求报文中未包含步骤节点");
        }

        JSONArray steps = reqJson.getJSONArray("steps");
        if (steps == null || steps.size() < 1) {
            throw new IllegalArgumentException("未包含步骤");
        }
        JSONObject step = null;
        JSONObject subStaff = null;
        for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
            step = steps.getJSONObject(stepIndex);

            Assert.hasKeyAndValue(step, "staffId", "步骤中未包含员工");
            Assert.hasKeyAndValue(step, "staffName", "步骤中未包含员工");
            Assert.hasKeyAndValue(step, "type", "步骤中类型会签还是正常流程");

            //正常流程
            if (WorkflowStepDto.TYPE_NORMAL.equals(step.getString("type"))) {
                continue;
            }

            //会签流程
            if (!step.containsKey("subStaff")) {
                throw new IllegalArgumentException("未包含会签员工信息");
            }

            JSONArray subStaffs = step.getJSONArray("subStaff");

            if (subStaffs == null || subStaffs.size() < 1) {
                throw new IllegalArgumentException("未包含会签员工信息");
            }

            for (int subStaffIndex = 0; subStaffIndex < subStaffs.size(); subStaffIndex++) {
                subStaff = subStaffs.getJSONObject(subStaffIndex);
                Assert.hasKeyAndValue(subStaff, "staffId", "会签中未包含员工");
                Assert.hasKeyAndValue(subStaff, "staffName", "会签中未包含员工");
            }
        }

    }

    /**
     * {"flowId":"752020061762800001","flowName":"投诉建议流程","describle":"xxxx",
     * "steps":[{"seq":0,"staffId":"302020060983230118","staffName":"孙印玉测试","type":"1",
     * "subStaff":[{"id":"d1aa0277-ff10-480e-855c-c90a1789909b","staffId":"302020061069500135","staffName":"天宫妹"}]},
     * {"seq":1,"staffId":"302020042299500115","staffName":"阿光","type":"2","subStaff":[]}]}"
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     */
    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        WorkflowStepDto workflowStepDto = new WorkflowStepDto();
        workflowStepDto.setFlowId(reqJson.getString("flowId"));
        workflowStepDto.setCommunityId(reqJson.getString("communityId"));
        List<WorkflowStepDto> workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);
        if (workflowStepDtos != null) {
            for (WorkflowStepDto tmpWorkflowStepDto : workflowStepDtos) {
                deleteWorkflowStepAndStaff(context, reqJson, tmpWorkflowStepDto);
            }
        }

        //修改 工作流程
        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setFlowId(reqJson.getString("flowId"));
        workflowPo.setFlowName(reqJson.getString("flowName"));
        workflowPo.setCommunityId(reqJson.getString("communityId"));
        workflowPo.setDescrible(reqJson.getString("describle"));
        super.update(context, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW);

        WorkflowDto workflowDto = BeanConvertUtil.covertBean(workflowPo, WorkflowDto.class);

        //保存 工作流程步骤
        JSONArray steps = reqJson.getJSONArray("steps");
        JSONObject step = null;
        JSONObject subStaff = null;
        WorkflowStepStaffPo workflowStepStaffPo = null;
        List<WorkflowStepDto> tmpWorkflowStepDtos = new ArrayList<>();
        for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
            step = steps.getJSONObject(stepIndex);
            WorkflowStepPo workflowStepPo = new WorkflowStepPo();
            workflowStepPo.setStepId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
            workflowStepPo.setCommunityId(workflowPo.getCommunityId());
            workflowStepPo.setFlowId(workflowPo.getFlowId());
            workflowStepPo.setSeq((stepIndex + 1) + "");
            workflowStepPo.setType(step.getString("type"));
            workflowStepPo.setStoreId(reqJson.getString("storeId"));
            super.insert(context, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP);
            WorkflowStepDto tmpWorkflowStepDto = BeanConvertUtil.covertBean(workflowStepPo, WorkflowStepDto.class);
            //正常流程
            List<WorkflowStepStaffDto> workflowStepStaffDtos = new ArrayList<>();
            if (WorkflowStepDto.TYPE_NORMAL.equals(step.getString("type"))) {
                workflowStepStaffPo = new WorkflowStepStaffPo();
                workflowStepStaffPo.setWssId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wssId));
                workflowStepStaffPo.setCommunityId(workflowPo.getCommunityId());
                workflowStepStaffPo.setStaffId(step.getString("staffId"));
                workflowStepStaffPo.setStaffName(step.getString("staffName"));
                workflowStepStaffPo.setStepId(workflowStepPo.getStepId());
                super.insert(context, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF);
                workflowStepStaffDtos.add(BeanConvertUtil.covertBean(workflowStepStaffPo, WorkflowStepStaffDto.class));
                continue;
            }

            JSONArray subStaffs = step.getJSONArray("subStaff");
            for (int subStaffIndex = 0; subStaffIndex < subStaffs.size(); subStaffIndex++) {
                subStaff = subStaffs.getJSONObject(subStaffIndex);
                workflowStepStaffPo = new WorkflowStepStaffPo();
                workflowStepStaffPo.setWssId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wssId));
                workflowStepStaffPo.setCommunityId(workflowPo.getCommunityId());
                workflowStepStaffPo.setStaffId(subStaff.getString("staffId"));
                workflowStepStaffPo.setStaffName(subStaff.getString("staffName"));
                workflowStepStaffPo.setStepId(workflowStepPo.getStepId());
                super.insert(context, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF);
                workflowStepStaffDtos.add(BeanConvertUtil.covertBean(workflowStepStaffPo, WorkflowStepStaffDto.class));
            }

            tmpWorkflowStepDto.setWorkflowStepStaffs(workflowStepStaffDtos);

            tmpWorkflowStepDtos.add(tmpWorkflowStepDto);
        }
        //提交
        commit(context);

        workflowDto.setWorkflowSteps(tmpWorkflowStepDtos);
        workflowInnerServiceSMOImpl.addFlowDeployment(workflowDto);
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK));
    }

    private void deleteWorkflowStepAndStaff(DataFlowContext context, JSONObject reqJson, WorkflowStepDto workflowStepDto) {
        WorkflowStepStaffDto workflowStepStaffDto = new WorkflowStepStaffDto();
        workflowStepStaffDto.setStepId(workflowStepDto.getStepId());
        workflowStepStaffDto.setCommunityId(workflowStepDto.getCommunityId());
        List<WorkflowStepStaffDto> workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);

        WorkflowStepPo workflowStepPo = new WorkflowStepPo();
        workflowStepPo.setCommunityId(workflowStepDto.getCommunityId());
        workflowStepPo.setStepId(workflowStepDto.getStepId());
        super.delete(context, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP);
        if (workflowStepStaffDtos == null) {
            return;
        }
        for (WorkflowStepStaffDto tmpWorkflowStepStaffDto : workflowStepStaffDtos) {
            WorkflowStepStaffPo workflowStepStaffPo = new WorkflowStepStaffPo();
            workflowStepStaffPo.setCommunityId(workflowStepDto.getCommunityId());
            workflowStepStaffPo.setWssId(tmpWorkflowStepStaffDto.getWssId());
            super.delete(context, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP_STAFF);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowConstant.UPDATE_WORKFLOW;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
