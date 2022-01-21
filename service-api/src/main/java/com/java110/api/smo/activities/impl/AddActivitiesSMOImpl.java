package com.java110.api.smo.activities.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.activities.IAddActivitiesSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addActivitiesSMOImpl")
public class AddActivitiesSMOImpl extends DefaultAbstractComponentSMO implements IAddActivitiesSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "title", "必填，请填写业活动标题");
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(paramIn, "typeCd", "必填，请选择活动类型");
        Assert.hasKeyAndValue(paramIn, "headerImg", "必填，请选择头部照片");
        Assert.hasKeyAndValue(paramIn, "context", "必填，请填写活动内容");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("userId", result.getUserId());
        paramIn.put("userName", result.getUserName());
        paramIn.put("storeId", result.getStoreId());

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "activities.saveActivities",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveActivities(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
