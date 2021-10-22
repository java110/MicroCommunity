package com.java110.api.smo.notice.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.notice.IEditNoticeSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加公告服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtNoticeSMOImpl")
public class EditNoticeSMOImpl extends DefaultAbstractComponentSMO implements IEditNoticeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "noticeId", "公告ID不能为空");
        Assert.hasKeyAndValue(paramIn, "title", "必填，请填写标题");
        Assert.hasKeyAndValue(paramIn, "noticeTypeCd", "必填，请选择公告类型");
        Assert.hasKeyAndValue(paramIn, "context", "必填，请填写公告内容");
        Assert.hasKeyAndValue(paramIn, "startTime", "必选，请填写开始时间 2019-01-02");
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区ID");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.HAS_LIST_NOTICE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("userId", pd.getUserId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "notice.updateNotice",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateNotice(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
