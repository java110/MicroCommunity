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
@Java110Listener("saveResourceStoreListener")
public class SaveResourceStoreListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IResourceStoreBMO resourceStoreBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "resName", "必填，请填写物品名称");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户信息");
        //Assert.hasKeyAndValue(reqJson, "resCode", "必填，请填写物品编码");
        Assert.hasKeyAndValue(reqJson, "price", "必填，请填写物品价格");
        /*Assert.hasKeyAndValue(reqJson, "stock", "必填，请填写物品库存");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写描述");*/

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        resourceStoreBMOImpl.addResourceStore(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreConstant.ADD_RESOURCESTORE;
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
