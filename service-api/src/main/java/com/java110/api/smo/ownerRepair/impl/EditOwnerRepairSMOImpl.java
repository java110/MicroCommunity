package com.java110.api.smo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.ownerRepair.IEditOwnerRepairSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加业主报修服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtOwnerRepairSMOImpl")
public class EditOwnerRepairSMOImpl extends DefaultAbstractComponentSMO implements IEditOwnerRepairSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区ID");
        Assert.hasKeyAndValue(paramIn, "repairId", "报修ID不能为空");
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
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONObject newParamIn = new JSONObject();
        newParamIn.put("communityId", paramIn.getString("communityId"));
        newParamIn.put("repairId", paramIn.getString("repairId"));
        newParamIn.put("page", 1);
        newParamIn.put("row", 1);

        //查询保修状态
        String apiUrl = "ownerRepair.listOwnerRepairs" + mapToUrlParam(newParamIn);
        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject outRepairInfo = JSONObject.parseObject(responseEntity.getBody());
        JSONObject repairObj = outRepairInfo.getJSONArray("data").getJSONObject(0);
        paramIn.put("state", repairObj.getString("state"));
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "ownerRepair.updateOwnerRepair",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateOwnerRepair(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
