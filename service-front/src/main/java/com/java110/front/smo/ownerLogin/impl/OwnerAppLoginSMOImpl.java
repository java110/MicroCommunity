package com.java110.front.smo.ownerLogin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.ownerLogin.IOwnerAppLoginSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * wx登录
 */
@Service("ownerAppLoginSMOImpl")
public class OwnerAppLoginSMOImpl extends AppAbstractComponentSMO implements IOwnerAppLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(OwnerAppLoginSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public ResponseEntity<String>
    doLogin(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "username", "请求报文中未包含用户名");
        Assert.hasKeyAndValue(paramIn, "password", "请求报文中未包含密码");
        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {

        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;

        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());

        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("password")));
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), ServiceConstant.SERVICE_API_URL + "/api/user.service.login", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());

        //根据用户查询商户信息
        String userId = userInfo.getString("userId");

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/owner.listAppUserBindingOwners?userid=" + userId, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject ownerInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        if (ownerInfo.getInteger("total") != 1) {
            responseEntity = new ResponseEntity<>("用户未绑定业主", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        JSONObject appUser = ownerInfo.getJSONArray("auditAppUserBindingOwners").getJSONObject(0);
        appUser.put("userId", userId);
        appUser.put("userName", paramIn.getString("username"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);
        paramOut.put("owner", appUser);
        paramOut.put("token", userInfo.getString("token"));
        //可能是app 登录 直接返回
        if (!paramIn.containsKey("code") || StringUtil.isEmpty(paramIn.getString("code"))) {
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }
        //如果code不为空调用微信接口获取openId 刷入到当前用户属性表

        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId"), HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONArray smallWeChats = ownerInfo.getJSONArray("smallWeChats");
        String appId = wechatAuthProperties.getAppId();
        String secret = wechatAuthProperties.getSecret();
        if (smallWeChats.size() > 0) {
            appId = smallWeChats.getJSONObject(0).getString("appId");
            secret = smallWeChats.getJSONObject(0).getString("appSecret");
        }

        String code = paramIn.getString("code");
        String urlString = "?appid={appId}&secret={secret}&js_code={code}&grant_type={grantType}";
        String response = outRestTemplate.getForObject(
                wechatAuthProperties.getSessionHost() + urlString, String.class,
                appId,
                secret,
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

        JSONObject userAttrInfo = new JSONObject();
        userAttrInfo.put("userId", userId);
        userAttrInfo.put("specCd", "100201911001");//微信openId
        userAttrInfo.put("value", openId);

        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, userAttrInfo.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/user.saveOrUpdateUserAttr", HttpMethod.POST);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
