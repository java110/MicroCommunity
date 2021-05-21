package com.java110.api.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStoreSpecification.IResourceStoreSpecificationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreSpecificationConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存物品规格侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateResourceStoreSpecificationListener")
public class UpdateResourceStoreSpecificationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreSpecificationBMO resourceStoreSpecificationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rssId", "rssId不能为空");
        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "rstId", "请求报文中未包含rstId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        resourceStoreSpecificationBMOImpl.updateResourceStoreSpecification(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreSpecificationConstant.UPDATE_RESOURCESTORESPECIFICATION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
