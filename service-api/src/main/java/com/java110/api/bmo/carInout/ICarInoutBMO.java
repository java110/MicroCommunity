package com.java110.api.bmo.carInout;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName ICarInoutBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 21:17
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface ICarInoutBMO extends IApiBaseBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void addCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加车辆进场信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void updateCarInout(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
