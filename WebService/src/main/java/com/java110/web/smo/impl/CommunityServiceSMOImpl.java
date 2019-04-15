package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.ICommunityServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 小区服务类
 */

@Service("communityServiceSMOImpl")
public class CommunityServiceSMOImpl extends BaseComponentSMO implements ICommunityServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(CommunityServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listMyCommunity(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        JSONObject _paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = super.getStoreInfo(pd,restTemplate);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(),"storeId","根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        //修改用户信息
        responseEntity = this.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.myCommunity.byMember?memberId="+storeId+
                        "&memberTypeCd="+MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE,storeTypeCd),
                HttpMethod.GET);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        responseEntity = new ResponseEntity<String>(JSONObject.parseObject(responseEntity.getBody().toString()).getJSONArray("communitys").toJSONString(),
                HttpStatus.OK);
        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
