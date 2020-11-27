package com.java110.api.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.community.ICommunityBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateCommunityListener")
public class UpdateCommunityListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ICommunityBMO communityBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写小区名称");
        Assert.hasKeyAndValue(reqJson, "address", "必填，请填写小区地址");
        Assert.hasKeyAndValue(reqJson, "nearbyLandmarks", "必填，请填写小区附近地标");

        Assert.judgeAttrValue(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        communityBMOImpl.updateCommunityOne(reqJson, context);

        if (!reqJson.containsKey("attrs")) {
            return;
        }

        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs.size() < 1) {
            return;
        }


        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                communityBMOImpl.addAttr(attr, context);
                continue;
            }
            communityBMOImpl.updateAttr(attr, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_COMMUNITY;
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
