package com.java110.api.bmo.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ITaskBMO extends IApiBaseBMO {


    /**
     * 添加定时任务
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addTask(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加定时任务信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateTask(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除定时任务
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteTask(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
