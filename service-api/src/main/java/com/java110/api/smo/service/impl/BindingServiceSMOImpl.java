package com.java110.api.smo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IBindingServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 绑定服务处理类
 */
@Service("bindingServiceSMOImpl")
public class BindingServiceSMOImpl extends DefaultAbstractComponentSMO implements IBindingServiceSMO {

    @Autowired
    private RestTemplate restTemplate;
    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "data", "未包含data节点请处理");

        JSONArray infos = paramIn.getJSONArray("data");

        if(infos == null || infos.size() !=3){
            throw new IllegalArgumentException("请求参数错误，为包含 应用, 服务或 扩展信息");
        }

        Assert.hasKeyByFlowData(infos, "addRouteView", "orderTypeCd", "必填，请填写订单类型");
        Assert.hasKeyByFlowData(infos, "addRouteView", "invokeLimitTimes", "必填，请填写调用次数");
        Assert.hasKeyByFlowData(infos, "addRouteView", "invokeModel", "可填，请填写消息队列，订单在异步调用时使用");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "service.bindingService",
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
