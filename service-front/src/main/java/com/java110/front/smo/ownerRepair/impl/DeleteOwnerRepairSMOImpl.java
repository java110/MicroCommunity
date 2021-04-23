package com.java110.front.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.front.smo.ownerRepair.IDeleteOwnerRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



/**
 * 添加小区服务实现类
 * delete by wuxw 2019-06-30
 */
@Service("deleteOwnerRepairSMOImpl")
public class DeleteOwnerRepairSMOImpl extends AbstractComponentSMO implements IDeleteOwnerRepairSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "repairId", "报修ID不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区ID");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_OWNERREPAIR);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/ownerRepair.deleteOwnerRepair",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteOwnerRepair(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
