package com.java110.api.listener.account;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.account.IAccountDetailBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAccountDetailConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存账户交易侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateAccountDetailListener")
public class UpdateAccountDetailListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAccountDetailBMO accountDetailBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");
        Assert.hasKeyAndValue(reqJson, "acctId", "请求报文中未包含acctId");
        Assert.hasKeyAndValue(reqJson, "detailType", "请求报文中未包含detailType");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        accountDetailBMOImpl.updateAccountDetail(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAccountDetailConstant.UPDATE_ACCOUNTDETAIL;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
