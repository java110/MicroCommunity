package com.java110.api.listener.inspectionPlan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPlanConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存巡检计划侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateInspectionPlanListener")
public class UpdateInspectionPlanListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IInspectionBMO inspectionBMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "inspectionPlanId不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanName", "必填，请填写巡检计划名称");
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "必填，请填写巡检路线");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanPeriod", "必填，请选择执行周期");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择计划开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(reqJson, "signType", "必填，请填写签到方式");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写签到方式");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

      inspectionBMOImpl.updateInspectionPlan(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPlanConstant.UPDATE_INSPECTION_PLAN;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}
