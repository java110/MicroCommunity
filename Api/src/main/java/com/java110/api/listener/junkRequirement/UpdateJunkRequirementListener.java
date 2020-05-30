package com.java110.api.listener.junkRequirement;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.junkRequirement.IJunkRequirementBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeJunkRequirementConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存旧货市场侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateJunkRequirementListener")
public class UpdateJunkRequirementListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IJunkRequirementBMO junkRequirementBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "junkRequirementId", "junkRequirementId不能为空");
//        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
//        Assert.hasKeyAndValue(reqJson, "classification", "请求报文中未包含classification");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
//        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
//        Assert.hasKeyAndValue(reqJson, "referencePrice", "请求报文中未包含referencePrice");
//        Assert.hasKeyAndValue(reqJson, "publishUserId", "请求报文中未包含publishUserId");
//        Assert.hasKeyAndValue(reqJson, "publishUserName", "请求报文中未包含publishUserName");
//        Assert.hasKeyAndValue(reqJson, "publishUserLink", "请求报文中未包含publishUserLink");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        JSONObject paramObj = new JSONObject();
        paramObj.put("junkRequirementId",reqJson.getString("junkRequirementId"));
        paramObj.put("communityId",reqJson.getString("communityId"));
        paramObj.put("state",reqJson.getString("state"));

        junkRequirementBMOImpl.updateJunkRequirement(paramObj, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeJunkRequirementConstant.UPDATE_JUNKREQUIREMENT;
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
