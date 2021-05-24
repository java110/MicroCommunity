package com.java110.api.bmo.resourceStoreSpecification;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IResourceStoreSpecificationBMO extends IApiBaseBMO {


    /**
     * 添加物品规格
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加物品规格信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除物品规格
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteResourceStoreSpecification(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
