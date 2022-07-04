package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.ICommunityServiceSMO;
import com.java110.api.smo.INavServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.util.CommonUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 导航栏业务处理类
 * Created by Administrator on 2019/4/1.
 */
@Service("navServiceSMOImpl")
public class NavServiceSMOImpl extends DefaultAbstractComponentSMO implements INavServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(NavServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;

    /**
     * 用户退出
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> doExit(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        JSONObject exitInfo = new JSONObject();
        exitInfo.put("token", pd.getToken());
        responseEntity = this.callCenterService(restTemplate, pd, exitInfo.toJSONString(),
                "user.service.logout", HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> getUserInfo(IPageData pd) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = super.getUserInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject tmpUserInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONObject resultUserInfo = new JSONObject();
        responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() == HttpStatus.OK && StringUtil.jsonHasKayAndValue(responseEntity.getBody().toString(),"storeTypeCd")) {
            String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
            resultUserInfo.put("storeTypeCd", storeTypeCd);
        }
        resultUserInfo.put("name", tmpUserInfo.getString("name"));
        resultUserInfo.put("address", tmpUserInfo.getString("address"));
        resultUserInfo.put("sex", tmpUserInfo.getString("sex"));
        resultUserInfo.put("localtionCd", tmpUserInfo.getString("localtionCd"));
        resultUserInfo.put("levelCd", tmpUserInfo.getString("levelCd"));
        //resultUserInfo.put("tel", CommonUtil.mobileEncrypt(tmpUserInfo.getString("tel")));
        resultUserInfo.put("tel", tmpUserInfo.getString("tel")); // 这里不加密了 因为前台很多地方直接 关联出 用户的手机号 所以 加密了 没法处理 modify by wuxw 2022-07-04
        resultUserInfo.put("email", tmpUserInfo.getString("email"));
        resultUserInfo.put("userId",tmpUserInfo.getString("userId"));
        String watermark = MappingCache.getValue("watermark");
        resultUserInfo.put("watermark",watermark);

        responseEntity = new ResponseEntity<String>(resultUserInfo.toJSONString(), HttpStatus.OK);

        return responseEntity;
    }


    @Override
    public ResponseEntity<String> listMyCommunity(IPageData pd) {
        ResponseEntity<String> responseEntity = communityServiceSMOImpl.listMyCommunity(pd);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONArray communitys = JSONArray.parseArray(responseEntity.getBody());

        JSONArray newCommunitys = new JSONArray();

        //只返回小区ID和小区名称 并且是在用的
        JSONObject tempCommunity = null;
        JSONObject newCommunity = null;
        for (int communityIndex = 0; communityIndex < communitys.size(); communityIndex++) {
            tempCommunity = communitys.getJSONObject(communityIndex);

            if (!StateConstant.AGREE_AUDIT.equals(tempCommunity.getString("auditStatusCd"))) {
                continue;
            }
            newCommunity = new JSONObject();
            newCommunity.put("communityId", tempCommunity.getString("communityId"));
            newCommunity.put("name", tempCommunity.getString("name"));
            newCommunitys.add(newCommunity);
        }
        responseEntity = new ResponseEntity<String>(newCommunitys.toJSONString(), HttpStatus.OK);
        return responseEntity;
    }

    public ICommunityServiceSMO getCommunityServiceSMOImpl() {
        return communityServiceSMOImpl;
    }

    public void setCommunityServiceSMOImpl(ICommunityServiceSMO communityServiceSMOImpl) {
        this.communityServiceSMOImpl = communityServiceSMOImpl;
    }
}
