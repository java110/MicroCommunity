package com.java110.api.smo.inspection.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.inspection.IAddInspectionPlanSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加巡检计划服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addInspectionPlanSMOImpl")
public class AddInspectionPlanSMOImpl extends DefaultAbstractComponentSMO implements IAddInspectionPlanSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "inspectionPlanName", "必填，请填写巡检计划名称");
        Assert.hasKeyAndValue(paramIn, "inspectionRouteId", "必填，请填写巡检路线");
        Assert.hasKeyAndValue(paramIn, "inspectionPlanPeriod", "必填，请选择执行周期");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择计划开始时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");
        Assert.hasKeyAndValue(paramIn, "signType", "必填，请填写签到方式");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写签到方式");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_INSPECTION_PLAN);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("createUserId", result.getUserId());
        paramIn.put("createUserName", result.getUserName());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "inspectionPlan.saveInspectionPlan",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveInspectionPlan(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
