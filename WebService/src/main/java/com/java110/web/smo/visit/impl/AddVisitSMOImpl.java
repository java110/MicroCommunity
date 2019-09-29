package com.java110.web.smo.visit.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.web.smo.visit.IAddVisitSMO;
import org.springframework.web.client.RestTemplate;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addVisitSMOImpl")
public class AddVisitSMOImpl extends AbstractComponentSMO implements IAddVisitSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        System.out.println(paramIn);

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(paramIn, "ownerId", "必填，请填写目标业主ID");
        Assert.hasKeyAndValue(paramIn, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(paramIn, "visitTime", "必填，请填写访客拜访时间");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_VISIT);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/visit.saveVisit",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveVisit(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
