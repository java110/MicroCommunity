package com.java110.api.bmo.ownerAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IOwnerAttrBMO extends IApiBaseBMO {


    /**
     * 添加业主属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加业主属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除业主属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
