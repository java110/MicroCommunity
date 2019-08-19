package com.java110.web.smo.menuGroup.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.menuGroup.IListMenuGroupsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询menuGroup服务类
 */
@Service("listMenuGroupsSMOImpl")
public class ListMenuGroupsSMOImpl extends AbstractComponentSMO implements IListMenuGroupsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listMenuGroups(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.MENU);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/menuGroup.listMenuGroups" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
