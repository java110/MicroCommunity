package com.java110.api.bmo.smallWeChat;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IWechatMenuBMO extends IApiBaseBMO {


    /**
     * 添加公众号菜单
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加公众号菜单信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除公众号菜单
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteWechatMenu(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
