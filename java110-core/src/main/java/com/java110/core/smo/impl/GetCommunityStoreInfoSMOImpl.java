package com.java110.core.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.cache.Java110RedisConfig;
import com.java110.core.context.IPageData;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.smo.IGetCommunityStoreInfoSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GetCommunityStoreInfoSMOImpl implements IGetCommunityStoreInfoSMO {

    private static final String BASE_PRIVILEGE = "JAVA110_BASE_PRIVILEGE";

    @Override
    @Cacheable(value = "getStoreInfo" + Java110RedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY, key = "#userId")
    public ResultVo getStoreInfo(IPageData pd, RestTemplate restTemplate, String userId) {


        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = CallApiServiceFactory.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/query.store.byuser?userId=" + userId, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, responseEntity.getBody());
    }

    @Override
    @Cacheable(value = "getStoreEnterCommunitys" + Java110RedisConfig.GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY, key = "#storeId")
    public ResultVo getStoreEnterCommunitys(IPageData pd, String storeId, String storeTypeCd, RestTemplate restTemplate) {
        ResponseEntity<String> responseEntity = null;
//        responseEntity = CallApiServiceFactory.callCenterService(restTemplate, pd, "",
//                ServiceConstant.SERVICE_API_URL + "/api/query.myCommunity.byMember?memberId=" + storeId + "&memberTypeCd="
//                        + MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeTypeCd), HttpMethod.GET);

        responseEntity = CallApiServiceFactory.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/communitys/queryStoreCommunitys?memberId=" + storeId + "&memberTypeCd="
                        + MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeTypeCd), HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, responseEntity.getBody());
    }

    @Override
    @Cacheable(value = "getUserPrivileges" + Java110RedisConfig.DEFAULT_EXPIRE_TIME_KEY, key = "#staffId")
    public ResultVo getUserPrivileges(IPageData pd, String staffId, String storeTypeCd, RestTemplate restTemplate) {

        ResponseEntity<String> privilegeGroup = CallApiServiceFactory.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/query.user.privilege?userId=" + staffId + "&domain=" + storeTypeCd, HttpMethod.GET);
        if (privilegeGroup.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(privilegeGroup.getBody());
        }
        return new ResultVo(privilegeGroup.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, privilegeGroup.getBody());

    }

    @Cacheable(value = "checkUserHasResourceListener" + Java110RedisConfig.DEFAULT_EXPIRE_TIME_KEY, key = "#cacheKey")
    public ResultVo checkUserHasResourceListener(RestTemplate restTemplate, IPageData pd, JSONObject paramIn, String cacheKey) {
        ResponseEntity<String> responseEntity = null;
        responseEntity = CallApiServiceFactory.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/basePrivilege.CheckUserHasResourceListener",
                HttpMethod.POST);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }

        JSONObject data = JSONObject.parseObject(responseEntity.getBody());

        JSONArray privileges = data.getJSONArray("privileges");

        return new ResultVo(responseEntity.getStatusCode() == HttpStatus.OK ? ResultVo.CODE_OK : ResultVo.CODE_ERROR, privileges.toJSONString());

    }
}
