package com.java110.api.bmo.parkingSpaceAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IParkingSpaceAttrBMO extends IApiBaseBMO {


    /**
     * 添加车位属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加车位属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除车位属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
