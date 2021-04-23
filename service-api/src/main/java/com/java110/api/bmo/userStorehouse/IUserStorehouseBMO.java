package com.java110.api.bmo.userStorehouse;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IUserStorehouseBMO extends IApiBaseBMO {


    /**
     * 添加个人物品
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加个人物品信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除个人物品
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
