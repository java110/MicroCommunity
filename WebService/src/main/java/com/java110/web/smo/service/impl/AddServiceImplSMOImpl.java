package com.java110.web.smo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.web.smo.service.IAddServiceImplSMO;
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
@Service("addServiceImplSMOImpl")
public class AddServiceImplSMOImpl extends AbstractComponentSMO implements IAddServiceImplSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        // Assert.hasKeyAndValue(paramIn, "serviceBusinessId", "必填，请填写应用ID");
Assert.hasKeyAndValue(paramIn, "businessTypeCd", "必填，请填写业务类型");
Assert.hasKeyAndValue(paramIn, "name", "必填，请填写业务名称");
Assert.hasKeyAndValue(paramIn, "invokeType", "必填，请填写调用类型");
Assert.hasKeyAndValue(paramIn, "url", "必填，请填写调用地址，为mapping 表中domain为DOMAIN.COMMON映射key");
Assert.hasKeyAndValue(paramIn, "timeout", "必填，请填写超时时间");
Assert.hasKeyAndValue(paramIn, "retryCount", "必填，请填写重试次数");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_SERVICEIMPL);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/serviceImpl.saveServiceImpl",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveServiceImpl(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
