package com.java110.api.listener.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.org.IOrgBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.org.IOrgInnerServiceSMO;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeOrgConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存组织管理侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateOrgListener")
public class UpdateOrgListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;
    @Autowired
    private IOrgBMO orgBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "orgId", "组织ID不能为空");
        Assert.hasKeyAndValue(reqJson, "orgName", "必填，请填写组织名称");
        Assert.hasKeyAndValue(reqJson, "orgLevel", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(reqJson, "parentOrgId", "必填，请选择上级ID");
        //Assert.hasKeyAndValue(reqJson, "description", "必填，请填写描述");
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        orgBMOImpl.updateOrg(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOrgConstant.UPDATE_ORG;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IOrgInnerServiceSMO getOrgInnerServiceSMOImpl() {
        return orgInnerServiceSMOImpl;
    }

    public void setOrgInnerServiceSMOImpl(IOrgInnerServiceSMO orgInnerServiceSMOImpl) {
        this.orgInnerServiceSMOImpl = orgInnerServiceSMOImpl;
    }
}
