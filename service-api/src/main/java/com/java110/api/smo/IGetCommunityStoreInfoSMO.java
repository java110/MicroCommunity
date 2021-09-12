package com.java110.api.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.vo.ResultVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface IGetCommunityStoreInfoSMO {

    ResultVo getStoreInfo(IPageData pd, RestTemplate restTemplate, String userId);

    ResultVo getStoreEnterCommunitys(IPageData pd, String storeId, String storeTypeCd, RestTemplate restTemplate);

    /**
     * 查询用户权限
     * @param pd
     * @param storeId
     * @param storeTypeCd
     * @param restTemplate
     * @return
     */
    ResultVo getUserPrivileges(IPageData pd, String storeId, String storeTypeCd, RestTemplate restTemplate);


    ResultVo checkUserHasResourceListener(RestTemplate restTemplate, IPageData pd, JSONObject paramIn, String cacheKey);
}
