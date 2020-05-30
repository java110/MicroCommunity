package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存巡检点
 * add by zc 2020-02-09
 */
@Java110Listener("saveInspectionPointListener")
public class SaveInspectionPointListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备ID");
        Assert.hasKeyAndValue(reqJson, "inspectionName", "必填，请填写巡检点名称");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

       inspectionBMOImpl.addInspectionPoint(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.ADD_INSPECTIONPOINT;
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
