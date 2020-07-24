package com.java110.front.smo.inspectionPoint.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.front.smo.inspectionPoint.IEditInspectionPointSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 编辑巡检点服务实现类
 * add by ZC 2020-02-08
 */
@Service("eidtInspectionPointSMOImpl")
public class EditInspectionPointSMOImpl extends AbstractComponentSMO implements IEditInspectionPointSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "inspectionId", "巡检点ID不能为空");
        Assert.hasKeyAndValue(paramIn, "inspectionName", "必填，请填写巡检点名称");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_INSPECTION_POINT);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/inspectionPoint.updateInspectionPoint",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateInspectionPoint(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
