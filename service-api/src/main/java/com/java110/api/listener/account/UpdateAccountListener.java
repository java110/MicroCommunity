package com.java110.api.listener.account;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.account.IAccountBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAccountConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存账户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateAccountListener")
public class UpdateAccountListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAccountBMO accountBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "acctId", "acctId不能为空");
        Assert.hasKeyAndValue(reqJson, "acctName", "请求报文中未包含acctName");
        Assert.hasKeyAndValue(reqJson, "acctType", "请求报文中未包含acctType");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        accountBMOImpl.updateAccount(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAccountConstant.UPDATE_ACCOUNT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
