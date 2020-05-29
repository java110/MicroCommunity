package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.owner.IOwnerBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.owner.IOwnerAppUserInnerServiceSMO;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存审核业主绑定侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateAppUserBindingOwnerListener")
public class UpdateAppUserBindingOwnerListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerBMO ownerBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {



        ownerBMOImpl.updateAuditAppUserBindingOwner(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.UPDATE_APPUSERBINDINGOWNER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IOwnerAppUserInnerServiceSMO getOwnerAppUserInnerServiceSMOImpl() {
        return ownerAppUserInnerServiceSMOImpl;
    }

    public void setOwnerAppUserInnerServiceSMOImpl(IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl) {
        this.ownerAppUserInnerServiceSMOImpl = ownerAppUserInnerServiceSMOImpl;
    }
}
