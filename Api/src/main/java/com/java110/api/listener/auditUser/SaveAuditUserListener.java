package com.java110.api.listener.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.auditUser.IAuditUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAuditUserConstant;


import com.java110.core.annotation.Java110Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAuditUserListener")
public class SaveAuditUserListener extends AbstractServiceApiPlusListener {
    @Autowired
    private IAuditUserBMO auditUserBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户");
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写用户名称");
        Assert.hasKeyAndValue(reqJson, "auditLink", "必填，请选择审核环节");
        Assert.hasKeyAndValue(reqJson, "objCode", "必填，请填写流程对象");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        auditUserBMOImpl.addAuditUser(reqJson, context);


    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAuditUserConstant.ADD_AUDITUSER;
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
