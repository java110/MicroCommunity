package com.java110.front.smo.serviceProvide.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.front.smo.serviceProvide.IAddServiceProvideSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addServiceProvideSMOImpl")
public class AddServiceProvideSMOImpl extends AbstractComponentSMO implements IAddServiceProvideSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写服务名称");
        Assert.hasKeyAndValue(paramIn, "serviceCode", "必填，请填写服务编码");
        Assert.hasKeyAndValue(paramIn, "params", "必填，请填写参数");
        Assert.hasKeyAndValue(paramIn, "queryModel", "必填，请选择是否显示菜单");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.SERVICE_PROVIDE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        /*responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/serviceProvide.saveServiceProvide",
                HttpMethod.POST);*/
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveServiceProvide(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
