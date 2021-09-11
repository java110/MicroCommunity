package com.java110.api.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.ownerRepair.ICloseRepairDispatchSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
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
@Service("closeRepairDispatchSMOImpl")
public class CloseRepairDispatchSMOImpl extends DefaultAbstractComponentSMO implements ICloseRepairDispatchSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        //JSONArray infos = paramIn.getJSONArray("data");
        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");

        Assert.hasKeyAndValue(paramIn, "state", "未包含处理信息");
        Assert.hasKeyAndValue(paramIn, "context", "未包含处理内容");
        Assert.hasKeyAndValue(paramIn, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.CLOSE_REPAIR_DISPATCH);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        paramIn.put("staffId", result.getUserId());

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                  ServiceCodeRepairDispatchStepConstant.CLOSE_REPAIR_DISPATCH,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> close(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
