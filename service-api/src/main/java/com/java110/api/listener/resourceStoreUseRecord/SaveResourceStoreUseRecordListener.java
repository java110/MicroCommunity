package com.java110.api.listener.resourceStoreUseRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.resourceStoreUseRecord.IResourceStoreUseRecordBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeResourceStoreUseRecordConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import com.java110.core.annotation.Java110Listener;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveResourceStoreUseRecordListener")
public class SaveResourceStoreUseRecordListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreUseRecordBMO resourceStoreUseRecordBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "resId", "请求报文中未包含resId");
        Assert.hasKeyAndValue(reqJson, "quantity", "请求报文中未包含quantity");
        Assert.hasKeyAndValue(reqJson, "unitPrice", "请求报文中未包含unitPrice");
        Assert.hasKeyAndValue(reqJson, "createUserId", "请求报文中未包含createUserId");
        Assert.hasKeyAndValue(reqJson, "createUserName", "请求报文中未包含createUserName");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        resourceStoreUseRecordBMOImpl.addResourceStoreUseRecord(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreUseRecordConstant.ADD_RESOURCESTOREUSERECORD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
