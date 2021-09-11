package com.java110.api.smo.inspection.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IEditInspectionRoutePointRelSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加巡检路线服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtInspectionRoutePointRelSMOImpl")
public class EditInspectionRoutePointRelSMOImpl extends DefaultAbstractComponentSMO implements IEditInspectionRoutePointRelSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "irpRelId", "路线巡检点ID不能为空");
        Assert.hasKeyAndValue(paramIn, "inspectionRouteId", "路线ID不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区ID不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "inspectionRoute.updateInspectionRoutePointRel",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateInspectionRoutePointRel(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
