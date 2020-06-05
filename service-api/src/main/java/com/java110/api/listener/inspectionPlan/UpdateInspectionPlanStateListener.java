package com.java110.api.listener.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IInspectionPlanInnerServiceSMO;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPlanConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存设备侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateInspectionPlanStateListener")
public class UpdateInspectionPlanStateListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "inspectionPlanId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        inspectionBMOImpl.updateInspectionPlanState(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPlanConstant.UPDATE_INSPECTION_PLAN_STATE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IInspectionPlanInnerServiceSMO getInspectionPlanInnerServiceSMOImpl() {
        return inspectionPlanInnerServiceSMOImpl;
    }

    public void setInspectionPlanInnerServiceSMOImpl(IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl) {
        this.inspectionPlanInnerServiceSMOImpl = inspectionPlanInnerServiceSMOImpl;
    }
}
