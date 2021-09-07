package com.java110.api.bmo.payFeeDetailDiscount;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IPayFeeDetailDiscountBMO extends IApiBaseBMO {


    /**
     * 添加缴费优惠
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    JSONObject addPayFeeDetailDiscount(JSONObject paramInJson,JSONObject discountJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void addPayFeeDetailDiscountTwo(JSONObject paramInJson, JSONObject discountJson, DataFlowContext dataFlowContext);

    /**
     * 添加缴费优惠信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updatePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除缴费优惠
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deletePayFeeDetailDiscount(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
