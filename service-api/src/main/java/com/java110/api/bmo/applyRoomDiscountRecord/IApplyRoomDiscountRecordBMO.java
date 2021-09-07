package com.java110.api.bmo.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IApplyRoomDiscountRecordBMO extends IApiBaseBMO {


    /**
     * 添加验房记录
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加验房记录信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除验房记录
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
