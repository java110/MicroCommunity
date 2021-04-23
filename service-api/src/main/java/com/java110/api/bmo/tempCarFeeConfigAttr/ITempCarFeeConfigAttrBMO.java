package com.java110.api.bmo.tempCarFeeConfigAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ITempCarFeeConfigAttrBMO extends IApiBaseBMO {


    /**
     * 添加临时车收费标准属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加临时车收费标准属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除临时车收费标准属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteTempCarFeeConfigAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
