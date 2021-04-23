package com.java110.api.bmo.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IWorkflowBMO extends IApiBaseBMO {


    /**
     * 添加工作流
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加工作流信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除工作流
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteWorkflow(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
