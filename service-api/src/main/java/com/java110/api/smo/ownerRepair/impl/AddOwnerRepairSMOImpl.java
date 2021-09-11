package com.java110.api.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.ownerRepair.IAddOwnerRepairSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
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
@Service("addOwnerRepairSMOImpl")
public class AddOwnerRepairSMOImpl extends DefaultAbstractComponentSMO implements IAddOwnerRepairSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区ID");
        Assert.hasKeyAndValue(paramIn, "repairType", "必填，请选择报修类型");
        Assert.hasKeyAndValue(paramIn, "repairName", "必填，请填写报修人名称");
        Assert.hasKeyAndValue(paramIn, "tel", "必填，请填写报修人手机号");
        Assert.hasKeyAndValue(paramIn, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(paramIn, "context", "必填，请填写报修内容");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_OWNERREPAIR);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult commonValidateResult = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        paramIn.put("userId", commonValidateResult.getUserId());
        paramIn.put("userName", commonValidateResult.getUserName());
        paramIn.put("storeId", commonValidateResult.getStoreId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "ownerRepair.saveOwnerRepair",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveOwnerRepair(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
