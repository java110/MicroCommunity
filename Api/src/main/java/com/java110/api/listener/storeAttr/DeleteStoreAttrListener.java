package com.java110.api.listener.storeAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.storeAttr.IStoreAttrBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeStoreAttrConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteStoreAttrListener")
public class DeleteStoreAttrListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IStoreAttrBMO storeAttrBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        storeAttrBMOImpl.deleteStoreAttr(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeStoreAttrConstant.DELETE_STOREATTR;
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
