package com.java110.api.smo.complaint.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.complaint.ISaveComplaintSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("saveComplaintSMOImpl")
public class SaveComplaintSMOImpl extends AppAbstractComponentSMO implements ISaveComplaintSMO {
    private final static Logger logger = LoggerFactory.getLogger(SaveComplaintSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> save(IPageData pd) {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {

        //查询用户ID
        paramIn.put("userId", pd.getUserId());
        //查询商户ID
        Map paramObj = new HashMap();
        paramObj.put("communityId", paramIn.getString("communityId"));
        paramObj.put("auditStatusCd", "1100");
        paramObj.put("memberTypeCd", "390001200002");
        paramObj.put("page", 1);
        paramObj.put("row", 1);
        String url = "store.listStoresByCommunity" + mapToUrlParam(paramObj);
        ResponseEntity<String> responseEntity = super.callCenterService(restTemplate, pd, "", url, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject storeObj = JSONObject.parseObject(responseEntity.getBody());
        JSONArray stores = storeObj.getJSONArray("stores");
        String storeId = stores.getJSONObject(0).getString("storeId");
        paramIn.put("storeId", storeId);
        url = "complaint.saveComplaint";
        responseEntity = super.callCenterService(restTemplate, pd, paramIn.toJSONString(), url, HttpMethod.POST);

        return responseEntity;
    }
}
