package com.java110.web.smo.applicationKey.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.smo.applicationKey.IEditApplicationKeySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加钥匙申请服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtApplicationKeySMOImpl")
public class EditApplicationKeySMOImpl extends AbstractComponentSMO implements IEditApplicationKeySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "applicationKeyId", "钥匙申请ID不能为空");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写姓名");
        Assert.hasKeyAndValue(paramIn, "tel", "必填，请填写手机号");
        Assert.hasKeyAndValue(paramIn, "typeCd", "必填，请选择用户类型");
        Assert.hasKeyAndValue(paramIn, "sex", "必填，请选择性别");
        Assert.hasKeyAndValue(paramIn, "age", "必填，请填写年龄");
        Assert.hasKeyAndValue(paramIn, "idCard", "必填，请填写身份证号");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_APPLICATION_KEY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/applicationKey.updateApplicationKey",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateApplicationKey(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
