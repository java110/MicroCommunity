package com.java110.front.smo.ownerLogin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.ownerLogin.IOwnerAppLoginSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * wx登录
 */
@Service("ownerAppLoginSMOImpl")
public class OwnerAppLoginSMOImpl extends AppAbstractComponentSMO implements IOwnerAppLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(OwnerAppLoginSMOImpl.class);

    private final static int expireTime = 7200;

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
    public ResponseEntity<String> getPageAccessToken(IPageData pd) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String state = paramIn.getString("state");
        String urlCode = CommonCache.getAndRemoveValue(paramIn.getString("urlCode"));

        if (StringUtil.isEmpty(urlCode)) {
            return ResultVo.redirectPage("/#/pages/login/login");
        }

        String url = WechatConstant.APP_GET_ACCESS_TOKEN_URL.replace("APPID", wechatAuthProperties.getWechatAppId())
                .replace("SECRET", wechatAuthProperties.getWechatAppSecret())
                .replace("CODE", authCode);

        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去token ", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/#/pages/login/login");

        }

        JSONObject paramObj = JSONObject.parseObject(paramOut.getBody());

        //获取 openId
        String openId = paramObj.getString("openid");
        //判断当前openId 是否绑定了业主

        pd = PageData.newInstance().builder("-1", "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/owner.listAppUserBindingOwners?openId=" + openId, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            //将openId放到redis 缓存，给前段下发临时票据
            String code = UUID.randomUUID().toString();
            CommonCache.setValue(code, openId, expireTime);
            return ResultVo.redirectPage("/#/pages/login/login?code=" + code);
        }
        JSONObject ownerInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        if (ownerInfo.getInteger("total") < 1) {
            //将openId放到redis 缓存，给前段下发临时票据
            String code = UUID.randomUUID().toString();
            CommonCache.setValue(code, openId, expireTime);
            return ResultVo.redirectPage("/#/pages/login/login?code=" + code);
        }

        // String accessToken = paramObj.getString("access_token");//暂时不用
        Map userMap = new HashMap();
        userMap.put(CommonConstant.LOGIN_USER_ID, ownerInfo.getString("userId"));
        userMap.put(CommonConstant.LOGIN_USER_NAME, ownerInfo.getString("appUserName"));
        String token = "";
        try {
            token = AuthenticationFactory.createAndSaveToken(userMap);
            pd.setToken(token);
        } catch (Exception e) {
            logger.error("创建token失败");
        }
        return ResultVo.redirectPage("/");

    }

    /**
     * 刷新token
     *
     * @param pd
     * @param redirectUrl
     * @param request
     * @param response
     * @return
     * @throws SMOException
     */
    @Override
    public ResponseEntity<String> refreshToken(IPageData pd, String redirectUrl, HttpServletRequest request, HttpServletResponse response) throws SMOException {
        //分配urlCode
        String urlCode = UUID.randomUUID().toString();
        CommonCache.setValue(urlCode, redirectUrl, expireTime);

        URL url = null;
        String openUrl = "";
        try {
            url = new URL(redirectUrl);

            String newUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() > 0) {
                newUrl += (":" + url.getPort());
            }

            openUrl = WechatConstant.OPEN_AUTH
                    .replace("APPID", wechatAuthProperties.getWechatAppId())
                    .replace("SCOPE", "snsapi_base")
                    .replace(
                            "REDIRECT_URL",
                            URLEncoder
                                    .encode(
                                            (newUrl
                                                    + "/app/loginOwnerWechatAuth?appId=992020061452450002&urlCode=" + urlCode),
                                            "UTF-8")).replace("STATE", "1");

        } catch (Exception e) {
            logger.error("微信公众号鉴权 redirectUrl 错误 " + redirectUrl, e);
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, e.getLocalizedMessage());
        }

        JSONObject urlObj = new JSONObject();
        urlObj.put("openUrl", openUrl);

        return ResultVo.createResponseEntity(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, urlObj);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        Assert.hasKeyAndValue(paramIn, "username", "请求报文中未包含用户名");
        Assert.hasKeyAndValue(paramIn, "password", "请求报文中未包含密码");
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

        if (ownerInfo.getInteger("total") < 1) {
            responseEntity = new ResponseEntity<>("用户未绑定业主", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        JSONArray auditAppUserBindingOwners = ownerInfo.getJSONArray("auditAppUserBindingOwners");

        JSONObject appUser = auditAppUserBindingOwners.getJSONObject(0);
        appUser.put("userId", userId);
        appUser.put("userName", paramIn.getString("username"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);
        paramOut.put("owner", appUser);
        paramOut.put("token", userInfo.getString("token"));

        String appId = pd.getAppId();

        if ("992020061452450002".equals(appId)) { //公众号
            return wechat(pd, paramIn, paramOut, userId, auditAppUserBindingOwners);
        } else if ("992019111758490006".equals(appId)) { //小程序
            return mina(pd, paramIn, paramOut, userId, auditAppUserBindingOwners);
        } else {//app
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }
    }

    /**
     * 公众号登录
     *
     * @param pd
     * @param paramIn
     * @param paramOut
     * @param userId
     * @param auditAppUserBindingOwners
     * @return
     */
    private ResponseEntity<String> wechat(IPageData pd, JSONObject paramIn, JSONObject paramOut, String userId,
                                          JSONArray auditAppUserBindingOwners) {

        ResponseEntity<String> responseEntity = null;
        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());


        String code = paramIn.getString("code");

        String openId = CommonCache.getValue(code);

        if (StringUtil.isEmpty(openId)) {
            responseEntity = new ResponseEntity<>("页面失效，请刷新后重试", HttpStatus.UNAUTHORIZED);
            return responseEntity;
        }

        JSONObject curOwnerApp = judgeCurrentOwnerBind(auditAppUserBindingOwners, OwnerAppUserDto.APP_TYPE_WECHAT);

        //说明 当前的openId 就是最新的
        if (curOwnerApp != null && openId.equals(curOwnerApp.getString("openId"))) {
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }

        JSONObject userOwnerInfo = new JSONObject();

        userOwnerInfo.put("openId", openId);
        userOwnerInfo.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT);
        if (curOwnerApp != null) {
            userOwnerInfo.put("appUserId", curOwnerApp.getString("appUserId"));
            userOwnerInfo.put("communityId", curOwnerApp.getString("communityId"));
        } else {
            userOwnerInfo.put("oldAppUserId", auditAppUserBindingOwners.getJSONObject(0).getString("appUserId"));
            userOwnerInfo.put("appUserId", "-1");
            userOwnerInfo.put("communityId", auditAppUserBindingOwners.getJSONObject(0).getString("communityId"));
        }

        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, userOwnerInfo.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/owner.refreshAppUserBindingOwnerOpenId", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    private ResponseEntity<String> mina(IPageData pd, JSONObject paramIn, JSONObject paramOut, String userId, JSONArray auditAppUserBindingOwners) {

        ResponseEntity<String> responseEntity = null;
        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1", HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");
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

        JSONObject curOwnerApp = judgeCurrentOwnerBind(auditAppUserBindingOwners, OwnerAppUserDto.APP_TYPE_WECHAT_MINA);

        //说明 当前的openId 就是最新的
        if (curOwnerApp != null && openId.equals(curOwnerApp.getString("openId"))) {
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }

        JSONObject userOwnerInfo = new JSONObject();

        userOwnerInfo.put("openId", openId);
        userOwnerInfo.put("appType", OwnerAppUserDto.APP_TYPE_WECHAT_MINA);
        if (curOwnerApp != null) {
            userOwnerInfo.put("appUserId", curOwnerApp.getString("appUserId"));
            userOwnerInfo.put("communityId", curOwnerApp.getString("communityId"));
        } else {
            userOwnerInfo.put("oldAppUserId", auditAppUserBindingOwners.getJSONObject(0).getString("appUserId"));
            userOwnerInfo.put("appUserId", "-1");
            userOwnerInfo.put("communityId", auditAppUserBindingOwners.getJSONObject(0).getString("communityId"));
        }

        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, userOwnerInfo.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/owner.refreshAppUserBindingOwnerOpenId", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    /**
     * 判断 绑定表里是否存在当前 端 绑定信息
     *
     * @param auditAppUserBindingOwners
     * @param appType
     * @return
     */
    private JSONObject judgeCurrentOwnerBind(JSONArray auditAppUserBindingOwners, String appType) {

        for (int appUserIndex = 0; appUserIndex < auditAppUserBindingOwners.size(); appUserIndex++) {
            if (appType.equals(auditAppUserBindingOwners.getJSONObject(appUserIndex).getString("appType"))) {
                return auditAppUserBindingOwners.getJSONObject(appUserIndex);
            }
        }
        return null;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
