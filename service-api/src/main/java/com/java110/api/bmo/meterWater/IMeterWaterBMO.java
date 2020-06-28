package com.java110.api.bmo.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IMeterWaterBMO extends IApiBaseBMO {


    /**
     * 添加水电费
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加水电费信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除水电费
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteMeterWater(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
