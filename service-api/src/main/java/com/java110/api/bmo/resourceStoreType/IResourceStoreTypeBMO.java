package com.java110.api.bmo.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IResourceStoreTypeBMO extends IApiBaseBMO {


    /**
     * 添加物品类型
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物品类型信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除物品类型
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteResourceStoreType(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
