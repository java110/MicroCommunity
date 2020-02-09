package com.java110.web.smo.inspectionRoute.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.smo.inspectionRoute.IAddInspectionRouteSMO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addInspectionRouteSMOImpl")
public class AddInspectionRouteSMOImpl extends AbstractComponentSMO implements IAddInspectionRouteSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "routeName", "必填，请填写路线名称，字数100个以内");
        Assert.hasKeyAndValue(paramIn, "inspectionName", "必填，请选择巡点名称");
        Assert.hasKeyAndValue(paramIn, "machineQuantity", "无需填写，系统自动生成");
        Assert.hasKeyAndValue(paramIn, "checkQuantity", "必填，请输入巡检路线的检查项数量");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_INSPECTIONROUTE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/inspectionRoute.saveInspectionRoute",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveInspectionRoute(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
