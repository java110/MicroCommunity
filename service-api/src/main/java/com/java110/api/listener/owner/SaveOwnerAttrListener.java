package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerAttr.IOwnerAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOwnerAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveOwnerAttrListener")
public class SaveOwnerAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerAttrBMO ownerAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        ownerAttrBMOImpl.addOwnerAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerAttrConstant.ADD_OWNERATTR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
