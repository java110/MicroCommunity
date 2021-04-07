package com.java110.api.bmo.storehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IStorehouseBMO extends IApiBaseBMO {


    /**
     * 添加仓库
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加仓库信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除仓库
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
