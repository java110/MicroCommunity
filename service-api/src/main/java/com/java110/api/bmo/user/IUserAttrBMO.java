package com.java110.api.bmo.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IUserAttrBMO extends IApiBaseBMO {


    /**
     * 添加用户属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加用户属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除用户属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
