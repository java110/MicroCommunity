package com.java110.api.smo.basePrivilege.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.basePrivilege.IDeleteBasePrivilegeSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * delete by wuxw 2019-06-30
 */
@Service("deleteBasePrivilegeSMOImpl")
public class DeleteBasePrivilegeSMOImpl extends DefaultAbstractComponentSMO implements IDeleteBasePrivilegeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "pId", "权限ID不能为空");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.BASE_PRIVILEGE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "basePrivilege.deleteBasePrivilege",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteBasePrivilege(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
