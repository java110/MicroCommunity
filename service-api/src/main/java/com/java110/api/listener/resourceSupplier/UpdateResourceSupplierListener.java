package com.java110.api.listener.resourceSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceSupplier.IResourceSupplierBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceSupplierConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存物品供应商侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateResourceSupplierListener")
public class UpdateResourceSupplierListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceSupplierBMO resourceSupplierBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "rsId", "rsId不能为空");
        Assert.hasKeyAndValue(reqJson, "supplierName", "请求报文中未包含supplierName");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        if (!reqJson.containsKey("storeId")) {
            String storeId = event.getDataFlowContext().getRequestCurrentHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        if (!reqJson.containsKey("createUserId")) {
            String userId = event.getDataFlowContext().getRequestCurrentHeaders().get("user-id");
            reqJson.put("createUserId", userId);
        }

        if (!reqJson.containsKey("createUserName")) {
            String userName = event.getDataFlowContext().getRequestCurrentHeaders().get("user-name");
            reqJson.put("createUserName", userName);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        resourceSupplierBMOImpl.updateResourceSupplier(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceSupplierConstant.UPDATE_RESOURCESUPPLIER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
