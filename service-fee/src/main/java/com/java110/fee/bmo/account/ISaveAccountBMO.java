package com.java110.fee.bmo.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.fee.bmo.IApiBaseBMO;

public interface ISaveAccountBMO extends IApiBaseBMO {

    /**
     * 添加账户余额
     * add by fqz
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return
     */
    void save(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加账户余额明细
     * add by fqz
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return
     */
    void saveDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

}
