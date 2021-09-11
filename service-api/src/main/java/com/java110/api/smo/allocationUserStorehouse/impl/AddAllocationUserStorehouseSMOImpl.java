package com.java110.api.smo.allocationUserStorehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.allocationUserStorehouse.IAddAllocationUserStorehouseSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加转赠记录实现类
 * add by fqz 2021-04-24
 */
@Service("addAllocationUserStorehouseSMOImpl")
public class AddAllocationUserStorehouseSMOImpl extends DefaultAbstractComponentSMO implements IAddAllocationUserStorehouseSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
       /* Assert.hasKeyAndValue(paramIn, "stock", "必填，请填现有库存数");
        Assert.hasKeyAndValue(paramIn, "giveQuantity", "必填，请填转赠数量");*/
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        paramIn.put("userId", pd.getUserId());
        paramIn.put("userName", pd.getUserName());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "resourceStore.saveAllocationUserStorehouse",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveAllocationUserStorehouse(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
