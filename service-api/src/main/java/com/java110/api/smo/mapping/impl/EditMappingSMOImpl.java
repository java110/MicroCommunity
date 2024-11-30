package com.java110.api.smo.mapping.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.mapping.IEditMappingSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加编码映射服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtMappingSMOImpl")
public class EditMappingSMOImpl extends DefaultAbstractComponentSMO implements IEditMappingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "id", "编码ID不能为空");
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
                "mapping.updateMapping",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateMapping(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
