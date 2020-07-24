package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 修改巡检点侦听
 * add by ZC 2020-02-08
 */
@Java110Listener("updateInspectionPointListener")
public class UpdateInspectionPointListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionBMO inspectionBMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点ID不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionName", "必填，请填写巡检点名称");
        Assert.hasKeyAndValue(reqJson, "pointObjId", "必填，请填写位置信息");
        Assert.hasKeyAndValue(reqJson, "pointObjType", "必填，请填写巡检类型");
        Assert.hasKeyAndValue(reqJson, "pointObjName", "必填，请填写位置信息");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        inspectionBMOImpl.updateInspectionPoint(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.UPDATE_INSPECTIONPOINT;
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
