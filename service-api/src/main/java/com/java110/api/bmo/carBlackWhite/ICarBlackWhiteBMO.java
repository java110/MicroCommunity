package com.java110.api.bmo.carBlackWhite;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;

/**
 * @ClassName ICarBlackWhiteBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:57
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface ICarBlackWhiteBMO extends IApiBaseBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void deleteCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext);


    /**
     * 添加黑白名单信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
