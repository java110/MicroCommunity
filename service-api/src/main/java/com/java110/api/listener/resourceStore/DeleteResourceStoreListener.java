package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStore.IResourceStoreBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteResourceStoreListener")
public class DeleteResourceStoreListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreBMO resourceStoreBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "resId", "物品ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "商户信息不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        resourceStoreBMOImpl.deleteResourceStore(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreConstant.DELETE_RESOURCESTORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


}
