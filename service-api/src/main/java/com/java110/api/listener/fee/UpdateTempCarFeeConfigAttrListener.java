package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.tempCarFeeConfigAttr.ITempCarFeeConfigAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存临时车收费标准属性侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateTempCarFeeConfigAttrListener")
public class UpdateTempCarFeeConfigAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITempCarFeeConfigAttrBMO tempCarFeeConfigAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        tempCarFeeConfigAttrBMOImpl.updateTempCarFeeConfigAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigAttrConstant.UPDATE_TEMPCARFEECONFIGATTR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
