package com.java110.api.bmo.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * 广告相关业务类
 */
public interface IAdvertBMO extends IApiBaseBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteAdvert(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
