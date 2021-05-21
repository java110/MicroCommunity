package com.java110.api.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStoreSpecification.IResourceStoreSpecificationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreSpecificationConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteResourceStoreSpecificationListener")
public class DeleteResourceStoreSpecificationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreSpecificationBMO resourceStoreSpecificationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "rssId", "rssId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        resourceStoreSpecificationBMOImpl.deleteResourceStoreSpecification(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreSpecificationConstant.DELETE_RESOURCESTORESPECIFICATION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
