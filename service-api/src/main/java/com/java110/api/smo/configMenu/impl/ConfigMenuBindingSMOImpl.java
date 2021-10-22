package com.java110.api.smo.configMenu.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.configMenu.IConfigMenuBindingSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceCodeConfigMenuConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("configMenuBindingSMOImpl")
public class ConfigMenuBindingSMOImpl extends DefaultAbstractComponentSMO implements IConfigMenuBindingSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        JSONArray infos = paramIn.getJSONArray("data");
        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyByFlowData(infos, "addMenuView", "name", "必填，请填写菜单名称");
        Assert.hasKeyByFlowData(infos, "addMenuView", "url", "必填，请菜单菜单地址");
        Assert.hasKeyByFlowData(infos, "addMenuView", "seq", "必填，请填写序列");
        Assert.hasKeyByFlowData(infos, "addMenuView", "isShow", "必填，请选择是否显示菜单");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "name", "必填，请填写权限名称");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "domain", "必填，请选择商户类型");
        Assert.hasKeyByFlowData(infos, "addPrivilegeView", "resource", "必填，请添加资源路径");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.MENU_MANAGE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                 ServiceCodeConfigMenuConstant.BINDING_CONFIGMENU,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> bindingConfigMenu(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
