package com.java110.web.smo.mapping.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.mapping.IAddMappingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addMappingSMOImpl")
public class AddMappingSMOImpl extends AbstractComponentSMO implements IAddMappingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "domain", "必填，请填写域");
Assert.hasKeyAndValue(paramIn, "name", "必填，请填写名称");
Assert.hasKeyAndValue(paramIn, "key", "必填，请填写键");
Assert.hasKeyAndValue(paramIn, "value", "必填，请填写值");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_MAPPING);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/mapping.saveMapping",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveMapping(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
