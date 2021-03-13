package com.java110.api.bmo.floorAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IFloorAttrBMO extends IApiBaseBMO {


    /**
     * 添加考勤班组属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加考勤班组属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除考勤班组属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
