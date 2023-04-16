package com.java110.fee.bmo.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.fee.bmo.IApiBaseBMO;

public interface IUpdateAccountBMO extends IApiBaseBMO {

    /**
     * 修改账户余额
     * add by fqz
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return
     */
    void update(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 账户处理
     * @param paramObj
     * @param dataFlowContext
     */
    void cashBackAccount(JSONObject paramObj, DataFlowContext dataFlowContext, JSONArray businesses);
}
