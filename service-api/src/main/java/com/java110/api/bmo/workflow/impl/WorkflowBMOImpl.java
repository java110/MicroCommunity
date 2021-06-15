package com.java110.api.bmo.workflow.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.workflow.IWorkflowBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowBMOImpl")
public class WorkflowBMOImpl extends ApiBaseBMO implements IWorkflowBMO {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("flowId", "-1");
        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.insert(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_WORKFLOW);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.update(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_WORKFLOW);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        WorkflowPo workflowPo = BeanConvertUtil.covertBean(paramInJson, WorkflowPo.class);
        super.update(dataFlowContext, workflowPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW);
    }

}
