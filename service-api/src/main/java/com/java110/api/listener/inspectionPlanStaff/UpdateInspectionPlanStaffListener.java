package com.java110.api.listener.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspectionPlanStaff.IInspectionPlanStaffBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPlanStaffConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存执行计划人侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateInspectionPlanStaffListener")
public class UpdateInspectionPlanStaffListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionPlanStaffBMO inspectionPlanStaffBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ipStaffId", "ipStaffId不能为空");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "staffName", "请求报文中未包含staffName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        inspectionPlanStaffBMOImpl.updateInspectionPlanStaff(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPlanStaffConstant.UPDATE_INSPECTIONPLANSTAFF;
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
