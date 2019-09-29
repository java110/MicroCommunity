package com.java110.web.smo.community.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.community.IAuditCommunitySMO;
import com.java110.web.smo.community.IAuditEnterCommunitySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 审核小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("auditEnterCommunitySMOImpl")
public class AuditEnterCommunitySMOImpl extends AbstractComponentSMO implements IAuditEnterCommunitySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "communityMemberId", "小区成员ID不能为空");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写小区审核状态");
        Assert.hasKeyAndValue(paramIn, "remark", "必填，请填写小区审核原因");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AUDIT_ENTER_COMMUNITY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        //super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        super.validateStoreStaffRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/community.auditEnterCommunity",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> auditEnterCommunity(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
