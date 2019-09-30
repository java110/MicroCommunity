package com.java110.web.smo.@@templateCode@@.impl;

import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.@@templateCode@@
import org.springframework.stereotype.Service;.IAdd@@TemplateCode@@SMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("@@templateCode@@BindingSMOImpl")
public class @@TemplateCode@@BindingSMOImpl extends AbstractComponentSMO implements I@@TemplateCode@@BindingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        JSONArray infos = paramIn.getJSONArray("data");
        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        @@validateTemplateColumns@@


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.@@TEMPLATECODE@@);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/" + ServiceCode@@TemplateCode@@Constant.BINDING_@@TEMPLATECODE@@,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> binding@@TemplateCode@@(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
