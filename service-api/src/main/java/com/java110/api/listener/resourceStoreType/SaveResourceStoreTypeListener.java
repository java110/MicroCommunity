package com.java110.api.listener.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStoreType.IResourceStoreTypeBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreTypeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveResourceStoreTypeListener")
public class SaveResourceStoreTypeListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreTypeBMO resourceStoreTypeBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含name");
        //Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

        if (!reqJson.containsKey("storeId")) {
            String storeId = event.getDataFlowContext().getRequestCurrentHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }

        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        resourceStoreTypeBMOImpl.addResourceStoreType(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreTypeConstant.ADD_RESOURCESTORETYPE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
