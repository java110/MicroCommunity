package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteAppUserBindingOwnerListener")
public class DeleteAppUserBindingOwnerListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");

        String env = MappingCache.getValue("HC_ENV");

        if ("DEV".equals(env) || "TEST".equals(env)) {
            throw new IllegalArgumentException("演示环境不能解绑，解绑后演示账号手机端无法登陆");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ownerBMOImpl.deleteAuditAppUserBindingOwner(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.DELETE_APPUSERBINDINGOWNER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }


}
