package com.java110.api.bmo.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

/**
 * @ClassName IOwnerRepairBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:19
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IOwnerRepairBMO extends IApiBaseBMO {

    public void modifyBusinessRepairUser(JSONObject paramInJson, DataFlowContext dataFlowContext);

    public void modifyBusinessRepair(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext);
    public void addBusinessRepairUser(JSONObject paramInJson, DataFlowContext dataFlowContext);
    public void modifyBusinessRepairDispatch(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext);
    /**
     * 添加业主报修信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext);
}
