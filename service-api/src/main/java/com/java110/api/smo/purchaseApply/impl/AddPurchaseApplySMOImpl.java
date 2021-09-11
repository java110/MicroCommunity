package com.java110.api.smo.purchaseApply.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.purchaseApply.IAddPurchaseApplySMO;
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
@Service("addPurchaseApplySMOImpl")
public class AddPurchaseApplySMOImpl extends DefaultAbstractComponentSMO implements IAddPurchaseApplySMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.hasKeyAndValue(paramIn, "description", "必填，请填申请说明");
        Assert.hasKeyAndValue(paramIn, "resOrderType", "必填，请填出入库类型");
        Assert.hasKeyAndValue(paramIn, "resourceStores", "必填，请填写申请物资信息");

        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_PURCHASE_APPLY);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        paramIn.put("userId", pd.getUserId());
        paramIn.put("userName", pd.getUserName());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "purchaseApply.savePurchaseApply",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> savePurchaseApply(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
