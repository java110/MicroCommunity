package com.java110.api.bmo.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ISmallWechatAttrBMO extends IApiBaseBMO {


    /**
     * 添加微信属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加微信属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除微信属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteSmallWechatAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
