package com.java110.api.bmo.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IAuditUserBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:49
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IAuditUserBMO extends IApiBaseBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void deleteAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     void addAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
