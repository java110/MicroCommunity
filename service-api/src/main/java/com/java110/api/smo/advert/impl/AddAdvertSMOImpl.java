package com.java110.api.smo.advert.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.advert.IAddAdvertSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("addAdvertSMOImpl")
public class AddAdvertSMOImpl extends DefaultAbstractComponentSMO implements IAddAdvertSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "adName", "必填，请填写广告名称");
        Assert.hasKeyAndValue(paramIn, "adTypeCd", "必填，请选择广告类型");
        Assert.hasKeyAndValue(paramIn, "classify", "必填，请选择广告分类");
        Assert.hasKeyAndValue(paramIn, "locationTypeCd", "必填，请选择投放位置");
        Assert.hasKeyAndValue(paramIn, "locationObjId", "必填，请填写具体位置");
        //Assert.hasKeyAndValue(paramIn, "state", "必填，请填写广告状态");
        Assert.hasKeyAndValue(paramIn, "seq", "必填，请填写播放顺序");
        Assert.hasKeyAndValue(paramIn, "startTime", "必填，请选择投放时间");
        Assert.hasKeyAndValue(paramIn, "endTime", "必填，请选择结束时间");

        if (!hasKeyAndValue(paramIn, "photos") && !hasKeyAndValue(paramIn, "vedioName")) {
            throw new IllegalArgumentException("请求报文中没有包含视频或图片");
        }


        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_ADVERT);

    }

    private boolean hasKeyAndValue(JSONObject paramIn, String key) {
        if (!paramIn.containsKey(key)) {
            return false;
        }

        if (StringUtil.isEmpty(paramIn.getString(key))) {
            return false;
        }

        return true;
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "advert.saveAdvert",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveAdvert(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
