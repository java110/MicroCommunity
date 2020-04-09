package com.java110.api.listener.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.org.IOrgBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeOrgConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveOrgCommunityListener")
public class SaveOrgCommunityListener extends AbstractServiceApiListener {
    @Autowired
    private IOrgBMO orgBMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "orgId", "必填，请填写组织ID");
        Assert.hasKeyAndValue(reqJson, "orgName", "必填，请填写组织名称");
        if (!reqJson.containsKey("communitys") || reqJson.getJSONArray("communitys").size() < 1) {
            throw new IllegalArgumentException("未包含小区信息");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        AppService service = event.getAppService();

        //添加单元信息
        JSONArray communitys = reqJson.getJSONArray("communitys");
        for (int communityIndex = 0; communityIndex < communitys.size(); communityIndex++) {
            businesses.add(orgBMOImpl.addOrgCommunity(reqJson, communitys.getJSONObject(communityIndex), communityIndex, context));
        }

        ResponseEntity<String> responseEntity = orgBMOImpl.callService(context, service.getServiceCode(), businesses);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOrgConstant.ADD_ORG_COMMUNITY;
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
