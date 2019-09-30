package com.java110.web.smo.menuGroup.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.menuGroup.IEditMenuGroupSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加菜单组服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtMenuGroupSMOImpl")
public class EditMenuGroupSMOImpl extends AbstractComponentSMO implements IEditMenuGroupSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "gId", "组Id不能为空");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写组名称");
        Assert.hasKeyAndValue(paramIn, "icon", "必填，请填写icon");
        Assert.hasKeyAndValue(paramIn, "label", "必填，请填写标签");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请填写序列");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.MENU);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/menuGroup.updateMenuGroup",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateMenuGroup(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
