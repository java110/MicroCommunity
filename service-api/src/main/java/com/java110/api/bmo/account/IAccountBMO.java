package com.java110.api.bmo.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IAccountBMO extends IApiBaseBMO {


    /**
     * 添加账户
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addAccount(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加账户信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateAccount(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除账户
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteAccount(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
