package com.java110.web.smo.resourceStore.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.resourceStore.IEditResourceStoreSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加物品管理服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtResourceStoreSMOImpl")
public class EditResourceStoreSMOImpl extends AbstractComponentSMO implements IEditResourceStoreSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "resId", "物品ID不能为空");
        Assert.hasKeyAndValue(paramIn, "resName", "必填，请填写物品名称");
        //Assert.hasKeyAndValue(paramIn, "resCode", "必填，请填写物品编码");
        Assert.hasKeyAndValue(paramIn, "price", "必填，请填写物品价格");
        Assert.hasKeyAndValue(paramIn, "stock", "必填，请填写物品库存");
        Assert.hasKeyAndValue(paramIn, "description", "必填，请填写描述");



        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_RESOURCESTORE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId",result.getStoreId());

        Map newParamIn = new HashMap();
        newParamIn.put("storeId",result.getStoreId());
        newParamIn.put("resId",paramIn.getString("resId"));

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/resourceStore.listResourceStores" + mapToUrlParam(paramIn);


        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }

        JSONArray resourceStores = JSONObject.parseObject(responseEntity.getBody()).getJSONArray("resourceStores");

        Assert.isOne(resourceStores,"查询物品数据存在多条或0条");

        paramIn.put("stock", resourceStores.getJSONObject(0).getString("stock"));

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/resourceStore.updateResourceStore",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateResourceStore(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
