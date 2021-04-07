package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.allocationStorehouse.IAllocationStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAllocationStorehouseListener")
public class SaveAllocationStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAllocationStorehouseBMO allocationStorehouseBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "shIda", "请求报文中未包含shIda");
        Assert.hasKeyAndValue(reqJson, "shIdz", "请求报文中未包含shIdz");
        Assert.hasKeyAndValue(reqJson, "resId", "请求报文中未包含resId");
        Assert.hasKeyAndValue(reqJson, "stock", "请求报文中未包含stock");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        allocationStorehouseBMOImpl.addAllocationStorehouse(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseConstant.ADD_ALLOCATIONSTOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
