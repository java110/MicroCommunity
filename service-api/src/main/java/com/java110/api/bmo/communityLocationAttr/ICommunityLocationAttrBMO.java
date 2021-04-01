package com.java110.api.bmo.communityLocationAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface ICommunityLocationAttrBMO extends IApiBaseBMO {


    /**
     * 添加位置属性
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     void addCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加位置属性信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void updateCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除位置属性
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
