package com.java110.api.listener.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.inspectionTask.IInspectionTaskBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionTaskConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveInspectionTaskListener")
public class SaveInspectionTaskListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IInspectionTaskBMO inspectionTaskBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");
        Assert.hasKeyAndValue(reqJson, "actInsTime", "请求报文中未包含actInsTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "actUserId", "请求报文中未包含actUserId");
        Assert.hasKeyAndValue(reqJson, "actUserName", "请求报文中未包含actUserName");
        Assert.hasKeyAndValue(reqJson, "signType", "请求报文中未包含signType");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        inspectionTaskBMOImpl.addInspectionTask(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionTaskConstant.ADD_INSPECTIONTASK;
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
