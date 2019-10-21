package com.java110.web.smo.resourceStore.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.resourceStore.IEditResourceStoreSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        Assert.hasKeyAndValue(paramIn, "resCode", "必填，请填写物品编码");
        Assert.hasKeyAndValue(paramIn, "price", "必填，请填写物品价格");
        Assert.hasKeyAndValue(paramIn, "stock", "必填，请填写物品库存");
        Assert.hasKeyAndValue(paramIn, "description", "必填，请填写描述");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_RESOURCESTORE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

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
