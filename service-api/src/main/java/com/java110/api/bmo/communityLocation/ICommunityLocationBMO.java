package com.java110.api.bmo.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ICommunityLocationBMO extends IApiBaseBMO {


    /**
     * 添加小区位置
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区位置信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除小区位置
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
