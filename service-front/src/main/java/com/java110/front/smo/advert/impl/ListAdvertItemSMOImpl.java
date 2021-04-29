package com.java110.front.smo.advert.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.front.smo.advert.IListAdvertItemSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service("listAdvertItemSMOImpl")
public class ListAdvertItemSMOImpl extends AbstractComponentSMO implements IListAdvertItemSMO {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "advertId", "请求报文中未包含广告");
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区");

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        paramIn.put("row", 50);
        paramIn.put("page", 1);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/advert.listAdvertItems" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listAdvertItems(IPageData pd) throws SMOException {
        return super.businessProcess(pd);
    }
}
