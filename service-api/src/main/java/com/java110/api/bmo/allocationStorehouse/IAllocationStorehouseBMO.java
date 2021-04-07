package com.java110.api.bmo.allocationStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IAllocationStorehouseBMO extends IApiBaseBMO {


    /**
     * 添加仓库调拨
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加仓库调拨信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除仓库调拨
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteAllocationStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
