package com.java110.api.bmo.ownerCarAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IOwnerCarAttrBMO extends IApiBaseBMO {


    /**
     * 添加业主车辆属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加业主车辆属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除业主车辆属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteOwnerCarAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
