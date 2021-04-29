package com.java110.api.bmo.allocationUserStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IAllocationUserStorehouseBMO extends IApiBaseBMO {


    /**
     * 添加物品供应商
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物品供应商信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除物品供应商
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
