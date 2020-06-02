package com.java110.api.bmo.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IStoreAttrBMO extends IApiBaseBMO {


    /**
     * 添加商户属性
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addStoreAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加商户属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateStoreAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除商户属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteStoreAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
