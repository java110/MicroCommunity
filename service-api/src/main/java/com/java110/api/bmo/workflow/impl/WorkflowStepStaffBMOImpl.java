package com.java110.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.workflow.IWorkflowStepStaffBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.po.workflow.WorkflowStepStaffPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowStepStaffBMOImpl")
public class WorkflowStepStaffBMOImpl extends ApiBaseBMO implements IWorkflowStepStaffBMO {

    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("wssId", "-1");
        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.insert(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW_STEP_STAFF);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.update(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW_STEP_STAFF);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteWorkflowStepStaff(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowStepStaffPo workflowStepStaffPo = BeanConvertUtil.covertBean(paramInJson, WorkflowStepStaffPo.class);
        super.delete(dataFlowContext, workflowStepStaffPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP_STAFF);
    }

}
