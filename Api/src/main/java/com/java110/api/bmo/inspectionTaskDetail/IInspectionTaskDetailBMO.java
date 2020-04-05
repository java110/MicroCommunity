package com.java110.api.bmo.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.IApiBaseBMO;
import com.java110.core.context.DataFlowContext;

public interface IInspectionTaskDetailBMO extends IApiBaseBMO {


    /**
     * 添加巡检任务明细
     * @param paramInJson
     * @param dataFlowContext
     * @return
     */
     JSONObject addInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);

    /**
     * 添加巡检任务明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject updateInspectionTaskDetail(JSONObject paramInJson, DataFlowContext dataFlowContext);




}
