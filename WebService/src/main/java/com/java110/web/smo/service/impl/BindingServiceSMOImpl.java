package com.java110.web.smo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.service.IBindingServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 绑定服务处理类
 */
@Service("bindingServiceSMOImpl")
public class BindingServiceSMOImpl extends AbstractComponentSMO implements IBindingServiceSMO {

    @Autowired
    private RestTemplate restTemplate;
    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "data", "未包含data节点请处理");

        JSONArray infos = paramIn.getJSONArray("data");

        if(infos == null || infos.size() !=2){
            throw new IllegalArgumentException("请求参数错误，为包含 应用或服务信息");
        }
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/service.bindingService",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> binding(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
