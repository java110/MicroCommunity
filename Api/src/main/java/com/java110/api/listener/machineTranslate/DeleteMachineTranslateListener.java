package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.machineTranslate.IMachineTranslateBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteMachineTranslateListener")
public class DeleteMachineTranslateListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMachineTranslateBMO machineTranslateBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "machineTranslateId", "同步ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        machineTranslateBMOImpl.deleteMachineTranslate(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.DELETE_MACHINETRANSLATE;
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
