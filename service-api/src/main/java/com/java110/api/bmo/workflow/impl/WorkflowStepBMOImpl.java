package com.java110.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.workflow.IWorkflowStepBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IWorkflowStepInnerServiceSMO;
import com.java110.po.workflow.WorkflowStepPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowStepBMOImpl")
public class WorkflowStepBMOImpl extends ApiBaseBMO implements IWorkflowStepBMO {

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("flowId", "-1");
        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.insert(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.update(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW_STEP);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteWorkflowStep(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowStepPo workflowStepPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepPo.class);
        super.update(dataFlowContext, workflowStepPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP);
    }

}
