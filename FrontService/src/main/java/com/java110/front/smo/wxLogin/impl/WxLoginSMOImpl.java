package com.java110.front.smo.wxLogin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.wxLogin.IWxLoginSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * wx登录
 */
@Service("wxLoginSMOImpl")
public class WxLoginSMOImpl extends AppAbstractComponentSMO implements IWxLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(WxLoginSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String> doLogin(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "code", "请求报文中未包含code信息");
        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {

        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;
        String code = paramIn.getString("code");
        String urlString = "?appid={appId}&secret={secret}&js_code={code}&grant_type={grantType}";
        String response = restTemplate.getForObject(
                wechatAuthProperties.getSessionHost() + urlString, String.class,
                wechatAuthProperties.getAppId(),
                wechatAuthProperties.getSecret(),
                code,
                wechatAuthProperties.getGrantType());

        logger.debug("wechatAuthProperties:" + JSONObject.toJSONString(wechatAuthProperties));

        logger.debug("微信返回报文：" + response);

        //Assert.jsonObjectHaveKey(response, "errcode", "返回报文中未包含 错误编码，接口出错");
        JSONObject responseObj = JSONObject.parseObject(response);

        if (responseObj.containsKey("errcode") && !"0".equals(responseObj.getString("errcode"))) {
            throw new IllegalArgumentException("微信验证失败，可能是code失效" + responseObj);
        }

        String openId = responseObj.getString("openid");
        String sessionKey = responseObj.getString("session_key");

        responseEntity = super.getUserInfoByOpenId(pd, restTemplate, openId);

        logger.debug("查询用户信息返回报文：" + responseEntity);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("根绝openId 查询用户信息异常" + openId + ", " + responseEntity.getBody());
        }

        JSONObject userResult = JSONObject.parseObject(responseEntity.getBody());
        int total = userResult.getIntValue("total");
        JSONObject paramOut = new JSONObject();
        JSONObject userInfo = paramIn.getJSONObject("userInfo");

        if (total == 0) {
            //保存用户信息
            /*JSONObject registerInfo = new JSONObject();

            //设置默认密码
            String userDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
            Assert.hasLength(userDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
            userDefaultPassword = AuthenticationFactory.passwdMd5(userDefaultPassword);

            registerInfo.put("userId", "-1");
            registerInfo.put("email", "");
            registerInfo.put("address", userInfo.getString("country") + userInfo.getString("province") + userInfo.getString("city"));
            registerInfo.put("locationCd", "001");
            registerInfo.put("age", "1");
            registerInfo.put("sex", "2".equals(userInfo.getString("gender")) ? "1" : "0");
            registerInfo.put("tel", "-1");
            registerInfo.put("level_cd", "1");
            registerInfo.put("name", userInfo.getString("nickName"));
            registerInfo.put("password", userDefaultPassword);
            JSONArray userAttr = new JSONArray();
            JSONObject userAttrObj = new JSONObject();
            userAttrObj.put("attrId", "-1");
            userAttrObj.put("specCd", "100201911001");
            userAttrObj.put("value", openId);
            userAttr.add(userAttrObj);
            registerInfo.put("businessUserAttr", userAttr);
            responseEntity = this.callCenterService(restTemplate, pd, registerInfo.toJSONString(), ServiceConstant.SERVICE_API_URL + "/api/user.service.register", HttpMethod.POST);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("保存用户信息失败");
            }
            responseEntity = super.getUserInfoByOpenId(pd, restTemplate, openId);

            logger.debug("查询用户信息返回报文：" + responseEntity);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("根绝openId 查询用户信息异常" + openId);
            }
            userResult = JSONObject.parseObject(responseEntity.getBody());*/
            paramOut.put("openId", openId);
            paramOut.put("msg", "还没有注册请先注册");
            responseEntity = new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.UNAUTHORIZED);

            return responseEntity;
        }

        JSONObject realUserInfo = userResult.getJSONArray("users").getJSONObject(0);
        userInfo.putAll(realUserInfo);
        userInfo.put("password", "");

        try {
            Map userMap = new HashMap();
            userMap.put(CommonConstant.LOGIN_USER_ID, userInfo.getString("userId"));
            userMap.put(CommonConstant.LOGIN_USER_NAME, userInfo.getString("name"));
            String token = AuthenticationFactory.createAndSaveToken(userMap);

            paramOut.put("result", 0);
            paramOut.put("userInfo", userInfo);
            paramOut.put("token", token);
            paramOut.put("sessionKey", sessionKey);
            pd.setToken(token);
            responseEntity = new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("登录异常：", e);
            throw new IllegalArgumentException("鉴权失败");
        }
        //根据openId 查询用户信息，是否存在用户
        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WechatAuthProperties getWechatAuthProperties() {
        return wechatAuthProperties;
    }

    public void setWechatAuthProperties(WechatAuthProperties wechatAuthProperties) {
        this.wechatAuthProperties = wechatAuthProperties;
    }
}
