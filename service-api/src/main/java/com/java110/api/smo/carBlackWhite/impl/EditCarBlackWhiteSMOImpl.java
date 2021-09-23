package com.java110.api.smo.carBlackWhite.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.carBlackWhite.IEditCarBlackWhiteSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 添加黑白名单服务实现类
 * add by wuxw 2019-06-30
 */
@Service("eidtCarBlackWhiteSMOImpl")
public class EditCarBlackWhiteSMOImpl extends DefaultAbstractComponentSMO implements IEditCarBlackWhiteSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);
        Assert.hasKeyAndValue(paramIn, "communityId", "必填，请填写车牌号");

        Assert.hasKeyAndValue(paramIn, "bwId", "黑白名单ID不能为空");
        Assert.hasKeyAndValue(paramIn, "blackWhite", "必填，请填写名单类型");
        Assert.hasKeyAndValue(paramIn, "carNum", "必填，请填写车牌号");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择开始时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST__CARBLACKWHITE);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "carBlackWhite.updateCarBlackWhite",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateCarBlackWhite(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
