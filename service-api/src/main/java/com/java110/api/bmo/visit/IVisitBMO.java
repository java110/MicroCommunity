package com.java110.api.bmo.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IVisitBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/10 0:05
 * @Version 1.0
 * add by wuxw 2020/3/10
 **/
public interface IVisitBMO extends IApiBaseBMO {
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteVisit(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void addVisit(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加访客登记信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateVisit(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
