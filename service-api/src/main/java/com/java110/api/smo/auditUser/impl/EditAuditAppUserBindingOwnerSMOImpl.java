package com.java110.api.smo.auditUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IEditAuditAppUserBindingOwnerSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加审核业主绑定服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtAuditAppUserBindingOwnerSMOImpl")
public class EditAuditAppUserBindingOwnerSMOImpl extends DefaultAbstractComponentSMO implements IEditAuditAppUserBindingOwnerSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "appUserId", "绑定ID不能为空");
        Assert.hasKeyAndValue(paramIn, "state", "必填，请填写状态");
        Assert.hasKeyAndValue(paramIn, "communityId", "未包含小区信息");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST__AUDITAPPUSERBINDINGOWNER);

        if(!"1100".equals(paramIn.getString("state")) && !"1200".equals(paramIn.getString("state"))){
            throw new IllegalArgumentException("审核状态错误");
        }

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        if("1100".equals(paramIn.getString("state"))){
            paramIn.put("state","12000");
        }else{
            paramIn.put("state","13000");
        }

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "owner.updateAppUserBindingOwner",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateAuditAppUserBindingOwner(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
