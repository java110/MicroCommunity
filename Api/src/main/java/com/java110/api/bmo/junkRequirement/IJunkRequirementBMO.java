package com.java110.api.bmo.junkRequirement;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;

public interface IJunkRequirementBMO extends IApiBaseBMO {


    /**
     * 添加旧货市场
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加旧货市场信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除旧货市场
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteJunkRequirement(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加图片
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    void  addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
