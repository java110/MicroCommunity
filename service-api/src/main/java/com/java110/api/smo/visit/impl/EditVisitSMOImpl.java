package com.java110.api.smo.visit.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.visit.IEditVisitSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加访客登记服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtVisitSMOImpl")
public class EditVisitSMOImpl extends DefaultAbstractComponentSMO implements IEditVisitSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        //super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "vId", "访客记录ID不能为空");
        Assert.hasKeyAndValue(paramIn, "vName", "必填，请填写访客姓名");
        Assert.hasKeyAndValue(paramIn, "visitGender", "必填，请填写访客性别");
        Assert.hasKeyAndValue(paramIn, "phoneNumber", "必填，请填写访客联系方式");
        Assert.hasKeyAndValue(paramIn, "visitTime", "必填，请填写访客来访时间");
        Assert.hasKeyAndValue(paramIn, "visitTime", "必填，请填写访客离开时间");
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_VISIT);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "visit.updateVisit",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateVisit(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
