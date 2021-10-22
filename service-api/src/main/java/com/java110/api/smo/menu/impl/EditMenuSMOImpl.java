package com.java110.api.smo.menu.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.menu.IEditMenuSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加菜单服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtMenuSMOImpl")
public class EditMenuSMOImpl extends DefaultAbstractComponentSMO implements IEditMenuSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "mId", "菜单ID不能为空");
        Assert.hasKeyAndValue(paramIn, "name", "必填，请填写菜单名称");
        Assert.hasKeyAndValue(paramIn, "url", "必填，请菜单菜单地址");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(paramIn, "isShow", "必填，请选择是否显示菜单");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.MENU_MANAGE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "menu.updateMenu",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateMenu(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
