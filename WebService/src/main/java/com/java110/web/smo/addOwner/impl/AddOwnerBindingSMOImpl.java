package com.java110.web.smo.addOwner.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceCodeAddOwnerConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.web.smo.addOwner.IAddOwnerBindingSMO;
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
@Service("addOwnerBindingSMOImpl")
public class AddOwnerBindingSMOImpl extends AbstractComponentSMO implements IAddOwnerBindingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        JSONArray infos = paramIn.getJSONArray("data");
        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyByFlowData(infos, "addOwner", "name", "必填，名称不能为空");
        Assert.hasKeyByFlowData(infos, "addOwner", "sex", "必填，请选择性别");
        Assert.hasKeyByFlowData(infos, "addOwner", "age", "必填，请填写年龄");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_ROOM);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/" + ServiceCodeAddOwnerConstant.BINDING_ADDOWNER,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> bindingAddOwner(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
