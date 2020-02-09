package com.java110.web.smo.inspectionRoute.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.smo.inspectionRoute.IEditInspectionRouteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加巡检路线服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtInspectionRouteSMOImpl")
public class EditInspectionRouteSMOImpl extends AbstractComponentSMO implements IEditInspectionRouteSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "configId", "inspectionRouteId不能为空");
        Assert.hasKeyAndValue(paramIn, "routeName", "必填，请填写路线名称，字数100个以内");
        Assert.hasKeyAndValue(paramIn, "inspectionName", "必填，请选择巡点名称");
        Assert.hasKeyAndValue(paramIn, "machineQuantity", "无需填写，系统自动生成");
        Assert.hasKeyAndValue(paramIn, "checkQuantity", "必填，请输入巡检路线的检查项数量");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST__INSPECTIONROUTE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/inspectionRoute.updateInspectionRoute",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateInspectionRoute(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
