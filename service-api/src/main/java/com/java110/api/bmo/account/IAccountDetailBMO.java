package com.java110.api.bmo.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IAccountDetailBMO extends IApiBaseBMO {


    /**
     * 添加账户交易
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加账户交易信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除账户交易
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteAccountDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 账户处理
     * @param paramObj
     * @param dataFlowContext
     */
    void dealAccount(JSONObject paramObj, DataFlowContext dataFlowContext, JSONArray businesses);
}
