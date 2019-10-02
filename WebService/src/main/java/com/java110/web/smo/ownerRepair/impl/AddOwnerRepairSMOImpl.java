package com.java110.web.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.ownerRepair.IAddOwnerRepairSMO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addOwnerRepairSMOImpl")
public class AddOwnerRepairSMOImpl extends AbstractComponentSMO implements IAddOwnerRepairSMO {

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
        Assert.hasKeyAndValue(paramIn, "roomId", "必填，请填写房屋ID");
        Assert.hasKeyAndValue(paramIn, "appointmentTime", "必填，请填写预约时间");
        Assert.hasKeyAndValue(paramIn, "context", "必填，请填写报修内容");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_OWNERREPAIR);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/ownerRepair.saveOwnerRepair",
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
