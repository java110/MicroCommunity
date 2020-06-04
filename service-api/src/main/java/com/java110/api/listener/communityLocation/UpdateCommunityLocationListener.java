package com.java110.api.listener.communityLocation;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.communityLocation.ICommunityLocationBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeCommunityLocationConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存小区位置侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateCommunityLocationListener")
public class UpdateCommunityLocationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICommunityLocationBMO communityLocationBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");

        Assert.hasKeyAndValue(reqJson, "locationId", "locationId不能为空");
        Assert.hasKeyAndValue(reqJson, "locationName", "请求报文中未包含locationName");
        Assert.hasKeyAndValue(reqJson, "locationType", "请求报文中未包含locationType");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        communityLocationBMOImpl.updateCommunityLocation(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeCommunityLocationConstant.UPDATE_COMMUNITYLOCATION;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
