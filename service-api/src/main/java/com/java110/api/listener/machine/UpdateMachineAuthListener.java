package com.java110.api.listener.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineAuth.IMachineAuthBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineAuthConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;



/**
 * 保存设备权限侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateMachineAuthListener")
public class UpdateMachineAuthListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMachineAuthBMO machineAuthBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "authId", "authId不能为空");
        Assert.hasKeyAndValue(reqJson, "machineId", "请求报文中未包含machineId");
        Assert.hasKeyAndValue(reqJson, "personId", "请求报文中未包含personId");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        machineAuthBMOImpl.updateMachineAuth(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineAuthConstant.UPDATE_MACHINEAUTH;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
