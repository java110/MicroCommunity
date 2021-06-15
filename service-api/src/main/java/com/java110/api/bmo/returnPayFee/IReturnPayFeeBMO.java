package com.java110.api.bmo.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IReturnPayFeeBMO extends IApiBaseBMO {


    /**
     * 添加退费表
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加退费表信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext);
    void updateFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    void addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除退费表
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
