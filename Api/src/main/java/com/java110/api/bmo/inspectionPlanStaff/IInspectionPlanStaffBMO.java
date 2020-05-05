package com.java110.api.bmo.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IInspectionPlanStaffBMO extends IApiBaseBMO {


    /**
     * 添加执行计划人
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     JSONObject addInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加执行计划人信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject updateInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 删除执行计划人
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject deleteInspectionPlanStaff(JSONObject paramInJson, DataFlowContext dataFlowContext);



}
