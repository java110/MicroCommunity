package com.java110.api.bmo.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ISmallWeChatBMO extends IApiBaseBMO {


    /**
     * 添加小程序管理
     *
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
    void addSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小程序管理信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除小程序管理
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteSmallWeChat(JSONObject paramInJson, DataFlowContext dataFlowContext);


}
