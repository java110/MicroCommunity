package com.java110.api.bmo.parkingArea;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IParkingAreaBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:23
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IParkingAreaBMO extends IApiBaseBMO {
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加停车场信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateParkingArea(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
