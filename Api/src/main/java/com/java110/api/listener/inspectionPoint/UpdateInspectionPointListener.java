package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备信息");

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
