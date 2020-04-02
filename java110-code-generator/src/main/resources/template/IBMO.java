package com.java110.api.bmo.activities;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface I@@TemplateCode@@BMO extends IApiBaseBMO {


    /**
     * 添加@@templateName@@
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     JSONObject add@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加@@templateName@@信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject update@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除@@templateName@@
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject delete@@TemplateCode@@(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
