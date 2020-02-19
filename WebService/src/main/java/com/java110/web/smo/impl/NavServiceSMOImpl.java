package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.StateConstant;
import com.java110.utils.util.CommonUtil;
import com.java110.core.context.IPageData;
import com.java110.core.component.BaseComponentSMO;
import com.java110.utils.util.StringUtil;
import com.java110.web.smo.ICommunityServiceSMO;
import com.java110.web.smo.INavServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class NavServiceSMOImpl extends BaseComponentSMO implements INavServiceSMO {
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
                ServiceConstant.SERVICE_API_URL + "/api/user.service.logout", HttpMethod.POST);

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
        resultUserInfo.put("tel", CommonUtil.mobileEncrypt(tmpUserInfo.getString("tel")));
        resultUserInfo.put("email", tmpUserInfo.getString("email"));
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
