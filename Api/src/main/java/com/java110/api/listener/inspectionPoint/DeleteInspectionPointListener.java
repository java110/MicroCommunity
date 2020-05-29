package com.java110.api.listener.inspectionPoint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.BusinessTypeConstant;

import com.java110.core.annotation.Java110Listener;
import com.java110.utils.constant.ServiceCodeInspectionPointConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 删除巡检点侦听
 * add by zc 2020-02-10
 */
@Java110Listener("deleteInspectionPointListener")
public class DeleteInspectionPointListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IInspectionBMO inspectionBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "inspectionId", "巡检点ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        inspectionBMOImpl.deleteInspectionPoint(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPointConstant.DELETE_INSPECTIONPOINT;
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
