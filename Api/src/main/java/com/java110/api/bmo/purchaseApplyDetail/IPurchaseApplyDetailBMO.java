package com.java110.api.bmo.purchaseApplyDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IPurchaseApplyDetailBMO extends IApiBaseBMO {


    /**
     * 添加订单明细
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addPurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加订单明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updatePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除订单明细
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deletePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
