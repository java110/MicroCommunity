package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.tempCarFeeConfig.ITempCarFeeConfigBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存临时车收费标准侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateTempCarFeeConfigListener")
public class UpdateTempCarFeeConfigListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITempCarFeeConfigBMO tempCarFeeConfigBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        tempCarFeeConfigBMOImpl.updateTempCarFeeConfig(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigConstant.UPDATE_TEMPCARFEECONFIG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
