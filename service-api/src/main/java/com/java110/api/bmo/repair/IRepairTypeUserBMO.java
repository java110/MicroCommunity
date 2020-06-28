package com.java110.api.bmo.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IRepairTypeUserBMO extends IApiBaseBMO {


    /**
     * 添加报修设置
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加报修设置信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除报修设置
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
