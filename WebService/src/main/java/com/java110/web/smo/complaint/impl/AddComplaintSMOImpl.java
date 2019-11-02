package com.java110.web.smo.complaint.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.smo.complaint.IAddComplaintSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addComplaintSMOImpl")
public class AddComplaintSMOImpl extends AbstractComponentSMO implements IAddComplaintSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        // Assert.hasKeyAndValue(paramIn, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(paramIn, "typeCd", "必填，请选择投诉类型");
        Assert.hasKeyAndValue(paramIn, "roomId", "必填，请选择房屋编号");
        Assert.hasKeyAndValue(paramIn, "complaintName", "必填，请填写投诉人");
        Assert.hasKeyAndValue(paramIn, "tel", "必填，请填写投诉电话");
        //Assert.hasKeyAndValue(paramIn, "state", "必填，请填写投诉状态");
        Assert.hasKeyAndValue(paramIn, "context", "必填，请填写投诉内容");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_COMPLAINT);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/complaint.saveComplaint",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveComplaint(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
