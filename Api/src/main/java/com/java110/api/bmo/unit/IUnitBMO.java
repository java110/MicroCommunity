package com.java110.api.bmo.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IUnitBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:54
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IUnitBMO extends IApiBaseBMO {
    /**
     * 修改小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void editUnit(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void addUnit(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 修改小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void editUpdateUnit(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
