package com.java110.api.smo.staff.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.Environment;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.staff.IChangeStaffPwdServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询carInout服务类
 */
@Service("changeStaffPwdSMOImpl")
public class ChangeStaffPwdSMOImpl extends DefaultAbstractComponentSMO implements IChangeStaffPwdServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> change(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        //Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(paramIn, "oldPwd", "必填，请填写原始密码");
        Assert.hasKeyAndValue(paramIn, "newPwd", "必填，请填写新密码");

        Environment.isDevEnv();
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

//        paramIn.put("oldPwd", AuthenticationFactory.passwdMd5(paramIn.getString("oldPwd")));
//        paramIn.put("newPwd", AuthenticationFactory.passwdMd5(paramIn.getString("newPwd")));

        String apiUrl = "user.changeStaffPwd";

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
