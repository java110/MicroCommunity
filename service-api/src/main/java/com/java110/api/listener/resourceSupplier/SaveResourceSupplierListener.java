package com.java110.api.listener.resourceSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceSupplier.IResourceSupplierBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceSupplierConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import com.java110.core.annotation.Java110Listener;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveResourceSupplierListener")
public class SaveResourceSupplierListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceSupplierBMO resourceSupplierBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "supplierName", "请求报文中未包含supplierName");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        if (!reqJson.containsKey("storeId")) {
            String storeId = event.getDataFlowContext().getRequestCurrentHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        resourceSupplierBMOImpl.addResourceSupplier(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceSupplierConstant.ADD_RESOURCESUPPLIER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
