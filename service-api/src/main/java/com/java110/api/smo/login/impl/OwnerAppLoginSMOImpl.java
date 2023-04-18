package com.java110.api.smo.login.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.login.IOwnerAppLoginSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarOpenUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * wx登录
 */
@Service("ownerAppLoginSMOImpl")
public class OwnerAppLoginSMOImpl extends DefaultAbstractComponentSMO implements IOwnerAppLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(OwnerAppLoginSMOImpl.class);

    private final static int expireTime = 7200;

    private final static int LOGIN_PAGE = 1;
    private final static int COMMON_PAGE = 2;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> doLogin(IPageData pd) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "username", "请求报文中未包含用户名");
        Assert.hasKeyAndValue(paramIn, "password", "请求报文中未包含密码");
        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;

        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());
        boolean loginByPhone = false;
        if (paramIn.containsKey("loginByPhone")) {
            loginByPhone = paramIn.getBoolean("loginByPhone");
        }

        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("password")));
        UserDto userDto = new UserDto();
        userDto.setUserName(loginInfo.getString("username"));
        userDto.setPassword(loginInfo.getString("password"));
        userDto.setLoginByPhone(loginByPhone);
        userDto.setLevelCd("02");
        userDto = super.postForApi(pd, userDto, ServiceCodeConstant.SERVICE_CODE_USER_LOGIN, UserDto.class);

        if (userDto == null) {
            responseEntity = new ResponseEntity<>("用户名或密码错误", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        //根据用户查询商户信息
        String userId = userDto.getUserId();

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = super.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);


        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            responseEntity = new ResponseEntity<>("用户未绑定业主", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        JSONObject appUser = JSONObject.parseObject(JSONObject.toJSONString(ownerAppUserDtos.get(0)));
        appUser.put("userId", userId);
        appUser.put("userName", paramIn.getString("username"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);
        paramOut.put("owner", appUser);
        paramOut.put("token", userDto.getToken());

        userDto = new UserDto();
        userDto.setUserId(ownerAppUserDtos.get(0).getUserId());
        UserDto tmpUserDto = super.getForApi(pd, userDto, ServiceCodeConstant.QUERY_USER_SECRET, UserDto.class);
        paramOut.put("key", tmpUserDto.getKey());

        String appId = pd.getAppId();

        if ("992020061452450002".equals(appId)) { //公众号
            return wechat(pd, paramIn, paramOut, userId, ownerAppUserDtos);
        } else if ("992019111758490006".equals(appId)) { //小程序
            return mina(pd, paramIn, paramOut, userId, ownerAppUserDtos);
        } else {//app
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<String> doLoginByKey(IPageData pd) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "key", "请求报文中未包含临时秘钥");
        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;

        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());

        UserDto userDto = new UserDto();
        userDto.setKey(paramIn.getString("key"));
        userDto = super.postForApi(pd, userDto, ServiceCodeConstant.SERVICE_CODE_USER_LOGIN, UserDto.class);


        //根据用户查询商户信息
        String userId = userDto.getUserId();

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        List<OwnerAppUserDto> ownerAppUserDtos = super.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);


        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            responseEntity = new ResponseEntity<>("用户未绑定业主", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        JSONObject appUser = JSONObject.parseObject(JSONObject.toJSONString(ownerAppUserDtos.get(0)));
        appUser.put("userId", userId);
        appUser.put("userName", paramIn.getString("username"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("code", 0);
        paramOut.put("msg", "成功");
        paramOut.put("owner", appUser);
        paramOut.put("token", userDto.getToken());
        paramOut.put("key", userDto.getKey());
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> getPageAccessToken(IPageData pd, HttpServletRequest request) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String state = paramIn.getString("state");
        String paramStr = CommonCache.getAndRemoveValue(paramIn.getString("urlCode"));

        if (StringUtil.isEmpty(paramStr)) {
            return ResultVo.redirectPage("/");
        }

        JSONObject param = JSONObject.parseObject(paramStr);
        String redirectUrl = param.getString("redirectUrl");
        String errorUrl = param.getString("errorUrl");
        String wId = paramIn.getString("wId");
        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wId)) {
            paramIn.put("appId", WechatFactory.getAppId(wId));
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String url = WechatConstant.APP_GET_ACCESS_TOKEN_URL.replace("APPID", smallWeChatDto.getAppId())
                .replace("SECRET", smallWeChatDto.getAppSecret())
                .replace("CODE", authCode);

        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId " + paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage(errorUrl);
        }
        JSONObject paramObj = JSONObject.parseObject(paramOut.getBody());

        //获取 openId
        String openId = paramObj.getString("openid");

        String userinfo_url = WechatConstant.APP_GET_USER_INFO_URL
                .replace("ACCESS_TOKEN", paramObj.getString("access_token"))
                .replace("OPENID", openId);

        ResponseEntity<String> userinfo_paramOut = outRestTemplate.getForEntity(userinfo_url, String.class);
        logger.debug("调用微信换去openId ", userinfo_paramOut);
        if (userinfo_paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");
        }
        JSONObject userinfo_paramObj = JSONObject.parseObject(userinfo_paramOut.getBody());

        //处理昵称有特殊符号导致 入库失败问题
        userinfo_paramObj.put("nickname", Base64Convert.byteToBase64(userinfo_paramObj.getString("nickname").getBytes()));

        //公众号未绑定 开放平台
        if (StringUtil.isEmpty(userinfo_paramObj.getString("unionid"))) {
            userinfo_paramObj.put("unionid", "-1");
        }

        int loginFlag = paramIn.getInteger("loginFlag");

        //说明是登录页面，下发code 就可以，不需要下发key 之类
        if (loginFlag == LOGIN_PAGE) {
            //将openId放到redis 缓存，给前段下发临时票据
            String code = UUID.randomUUID().toString();
            CommonCache.setValue(code, openId, expireTime);
            CommonCache.setValue(code + "-nickname", userinfo_paramObj.getString("nickname"), expireTime);
            CommonCache.setValue(code + "-headimgurl", userinfo_paramObj.getString("headimgurl"), expireTime);
            CommonCache.setValue(code + "-unionid", userinfo_paramObj.getString("unionid"), expireTime);
            if (errorUrl.indexOf("?") > 0) {
                errorUrl += ("&code=" + code);
            } else {
                errorUrl += ("?code=" + code);
            }
            logger.debug("登录跳转url:{}", errorUrl);

            return ResultVo.redirectPage(errorUrl);
        }

        if (loginFlag == COMMON_PAGE) {
            //将openId放到redis 缓存，给前段下发临时票据
            if (errorUrl.indexOf("?") > 0) {
                redirectUrl += ("&openId=" + openId);
            } else {
                redirectUrl += ("?openId=" + openId);
            }
            logger.debug("跳转url:{}", redirectUrl);
            return ResultVo.redirectPage(redirectUrl);
        }

        //判断当前openId 是否绑定了业主
        pd = PageData.newInstance().builder("-1", "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setOpenId(openId);
        List<OwnerAppUserDto> ownerAppUserDtos = super.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            //将openId放到redis 缓存，给前段下发临时票据
            String code = UUID.randomUUID().toString();
            CommonCache.setValue(code, openId, expireTime);
            CommonCache.setValue(code + "-nickname", userinfo_paramObj.getString("nickname"), expireTime);
            CommonCache.setValue(code + "-headimgurl", userinfo_paramObj.getString("headimgurl"), expireTime);
            CommonCache.setValue(code + "-unionid", userinfo_paramObj.getString("unionid"), expireTime);
            if (errorUrl.indexOf("?") > 0) {
                errorUrl += ("&code=" + code);
            } else {
                errorUrl += ("?code=" + code);
            }
            return ResultVo.redirectPage(errorUrl);
        }

        // String accessToken = paramObj.getString("access_token");//暂时不用
        Map userMap = new HashMap();
        userMap.put(CommonConstant.LOGIN_USER_ID, ownerAppUserDtos.get(0).getUserId());
        userMap.put(CommonConstant.LOGIN_USER_NAME, ownerAppUserDtos.get(0).getAppUserName());
        String token = "";
        try {
            token = AuthenticationFactory.createAndSaveToken(userMap);
            pd.setToken(token);
        } catch (Exception e) {
            logger.error("创建token失败");
        }
        //查询用户key
        UserDto userDto = new UserDto();
        userDto.setUserId(ownerAppUserDtos.get(0).getUserId());
        UserDto tmpUserDto = super.getForApi(pd, userDto, ServiceCodeConstant.QUERY_USER_SECRET, UserDto.class);

        if (StringUtil.isEmpty(tmpUserDto.getKey())) {
            String code = UUID.randomUUID().toString();
            CommonCache.setValue(code, openId, expireTime);
            CommonCache.setValue(code + "-nickname", userinfo_paramObj.getString("nickname"), expireTime);
            CommonCache.setValue(code + "-headimgurl", userinfo_paramObj.getString("headimgurl"), expireTime);
            CommonCache.setValue(code + "-unionid", userinfo_paramObj.getString("unionid"), expireTime);

            if (errorUrl.indexOf("?") > 0) {
                errorUrl += ("&code=" + code);
            } else {
                errorUrl += ("?code=" + code);
            }
            return ResultVo.redirectPage(errorUrl);
        }
        //如果参数中有key 直接用新的覆盖
        Map tempRedirectParam = super.urlToMap(redirectUrl);
        tempRedirectParam.put("key", tmpUserDto.getKey());
        if (redirectUrl.indexOf("?") > -1) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf("?")) + super.mapToUrlParam(tempRedirectParam);
        } else {
            redirectUrl = redirectUrl + super.mapToUrlParam(tempRedirectParam);
        }
        //redirectUrl = redirectUrl + (redirectUrl.indexOf("?") > 0 ? "&key=" + tmpUserDto.getKey() : "?key=" + tmpUserDto.getKey());
        return ResultVo.redirectPage(redirectUrl);

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
    public ResponseEntity<String> refreshToken(IPageData pd, String redirectUrl,
                                               String errorUrl,
                                               String loginFlag,
                                               HttpServletRequest request, HttpServletResponse response) throws SMOException {
        //分配urlCode
        String urlCode = UUID.randomUUID().toString();
        JSONObject param = new JSONObject();
        param.put("redirectUrl", redirectUrl);
        param.put("errorUrl", errorUrl);
        CommonCache.setValue(urlCode, param.toJSONString(), expireTime);
        String wAppId = request.getHeader("w-app-id");
        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wAppId)) {
            JSONObject paramIn = new JSONObject();
            paramIn.put("appId", wAppId);
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        URL url = null;
        String openUrl = "";
        try {
            url = new URL(redirectUrl);

            String newUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() > 0) {
                newUrl += (":" + url.getPort());
            }

            openUrl = WechatConstant.OPEN_AUTH
                    .replace("APPID", smallWeChatDto.getAppId())
                    .replace("SCOPE", "snsapi_userinfo")
                    .replace(
                            "REDIRECT_URL",
                            URLEncoder
                                    .encode(
                                            (newUrl
                                                    + "/app/loginOwnerWechatAuth?appId=992020061452450002&urlCode=" +
                                                    urlCode + "&loginFlag=" + loginFlag + "&wId=" + WechatFactory.getWId(wAppId)),
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
    public ResponseEntity<String> refreshOpenId(IPageData pd, String redirectUrl, String wAppId, HttpServletRequest request, HttpServletResponse response) {

        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wAppId)) {
            JSONObject paramIn = new JSONObject();
            paramIn.put("appId", wAppId);
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
            wAppId = wechatAuthProperties.getWechatAppId();
        }

        //分配urlCode
        String urlCode = UUID.randomUUID().toString();
        JSONObject param = new JSONObject();
        if (redirectUrl.indexOf("appId") < 0) {
            redirectUrl += ("&appId=" + smallWeChatDto.getAppId());
        }
        param.put("redirectUrl", redirectUrl);
        CommonCache.setValue(urlCode, param.toJSONString(), expireTime);

        URL url = null;
        String openUrl = "";
        try {
            url = new URL(redirectUrl);

            String newUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() > 0) {
                newUrl += (":" + url.getPort());
            }

            openUrl = WechatConstant.OPEN_AUTH
                    .replace("APPID", smallWeChatDto.getAppId())
                    .replace("SCOPE", "snsapi_base")
                    .replace(
                            "REDIRECT_URL",
                            URLEncoder
                                    .encode(
                                            (newUrl
                                                    + "/app/openServiceNotifyOpenId?appId=992020061452450002&urlCode=" +
                                                    urlCode + "&wId=" + WechatFactory.getWId(wAppId)),
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
    public ResponseEntity openServiceNotifyOpenId(IPageData pd, HttpServletRequest request) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String state = paramIn.getString("state");
        String paramStr = CommonCache.getAndRemoveValue(paramIn.getString("urlCode"));

        if (StringUtil.isEmpty(paramStr)) {
            return ResultVo.redirectPage("/");
        }

        JSONObject param = JSONObject.parseObject(paramStr);
        String redirectUrl = param.getString("redirectUrl");
        String wId = paramIn.getString("wId");
        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wId)) {
            paramIn.put("appId", WechatFactory.getAppId(wId));
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String url = WechatConstant.APP_GET_ACCESS_TOKEN_URL.replace("APPID", smallWeChatDto.getAppId())
                .replace("SECRET", smallWeChatDto.getAppSecret())
                .replace("CODE", authCode);

        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId " + paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");
        }
        JSONObject paramObj = JSONObject.parseObject(paramOut.getBody());
        //获取 openId
        String openId = paramObj.getString("openid");
        redirectUrl = redirectUrl + "&openId=" + openId;

        //查询是否有车牌号
        OwnerCarOpenUserDto ownerCarOpenUserDto = new OwnerCarOpenUserDto();
        ownerCarOpenUserDto.setOpenId(openId);
        List<OwnerCarOpenUserDto> ownerCarOpenUserDtos = ownerCarOpenUserV1InnerServiceSMOImpl.queryOwnerCarOpenUsers(ownerCarOpenUserDto);
        if (ownerCarOpenUserDtos != null && ownerCarOpenUserDtos.size() > 0) {
            redirectUrl += ("&carNum=" + ownerCarOpenUserDtos.get(0).getCarNum());
        }


        //redirectUrl = redirectUrl + (redirectUrl.indexOf("?") > 0 ? "&key=" + tmpUserDto.getKey() : "?key=" + tmpUserDto.getKey());
        return ResultVo.redirectPage(redirectUrl);
    }

    /**
     * 公众号登录
     *
     * @param pd
     * @param paramIn
     * @param paramOut
     * @param userId
     * @param ownerAppUserDtos
     * @return
     */
    private ResponseEntity<String> wechat(IPageData pd, JSONObject paramIn, JSONObject paramOut, String userId,
                                          List<OwnerAppUserDto> ownerAppUserDtos) {

        ResponseEntity<String> responseEntity = null;
        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());


        String code = paramIn.getString("code");

        String openId = CommonCache.getValue(code);
        String nickname = CommonCache.getValue(code + "-nickname");
        String headimgurl = CommonCache.getValue(code + "-headimgurl");
        String unionid = CommonCache.getValue(code + "-unionid");

        if (StringUtil.isEmpty(openId)) {
            responseEntity = new ResponseEntity<>("页面失效，请刷新后重试", HttpStatus.UNAUTHORIZED);
            return responseEntity;
        }

        OwnerAppUserDto curOwnerApp = judgeCurrentOwnerBind(ownerAppUserDtos, OwnerAppUserDto.APP_TYPE_WECHAT);

        //说明 当前的openId 就是最新的
        if (curOwnerApp != null && openId.equals(curOwnerApp.getOpenId())) {
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }

        JSONObject userOwnerInfo = new JSONObject();
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setOpenId(openId);
        ownerAppUserDto.setUnionId(unionid);
        // ownerAppUserDto.setNickName(StringUtil.encodeEmoji(nickname));
        ownerAppUserDto.setNickName(nickname);
        ownerAppUserDto.setHeadImgUrl(headimgurl);
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        if (curOwnerApp != null) {
            ownerAppUserDto.setAppUserId(curOwnerApp.getAppUserId());
            ownerAppUserDto.setCommunityId(curOwnerApp.getCommunityId());
        } else {
            ownerAppUserDto.setOldAppUserId(ownerAppUserDtos.get(0).getAppUserId());
            ownerAppUserDto.setAppUserId("-1");
            ownerAppUserDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        }

        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());

        super.postForApi(pd, ownerAppUserDto, ServiceCodeConstant.REFRESH_APP_USER_BINDING_OWNER_OPEN_ID,
                OwnerAppUserDto.class);
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    private ResponseEntity<String> mina(IPageData pd, JSONObject paramIn, JSONObject paramOut, String userId, List<OwnerAppUserDto> ownerAppUserDtos) {

        ResponseEntity<String> responseEntity = null;
        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1&communityId=" + ownerAppUserDtos.get(0).getCommunityId(), HttpMethod.GET);

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

        OwnerAppUserDto ownerAppUserDto = judgeCurrentOwnerBind(ownerAppUserDtos, OwnerAppUserDto.APP_TYPE_WECHAT_MINA);

        //说明 当前的openId 就是最新的
        if (ownerAppUserDto != null && openId.equals(ownerAppUserDto.getOpenId())) {
            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        }

        OwnerAppUserDto tmpOwnerAppUserDto = new OwnerAppUserDto();
        tmpOwnerAppUserDto.setOpenId(openId);
        tmpOwnerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT_MINA);
        if (ownerAppUserDto != null) {
            tmpOwnerAppUserDto.setAppUserId(ownerAppUserDto.getAppUserId());
            tmpOwnerAppUserDto.setCommunityId(ownerAppUserDto.getCommunityId());
        } else {
            tmpOwnerAppUserDto.setOldAppUserId(ownerAppUserDtos.get(0).getAppUserId());
            tmpOwnerAppUserDto.setAppUserId("-1");
            tmpOwnerAppUserDto.setCommunityId(ownerAppUserDtos.get(0).getCommunityId());
        }
        //查询微信信息
        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());

        super.postForApi(pd, tmpOwnerAppUserDto, ServiceCodeConstant.REFRESH_APP_USER_BINDING_OWNER_OPEN_ID,
                OwnerAppUserDto.class);
        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    /**
     * 判断 绑定表里是否存在当前 端 绑定信息
     *
     * @param ownerAppUserDtos
     * @param appType
     * @return
     */
    private OwnerAppUserDto judgeCurrentOwnerBind(List<OwnerAppUserDto> ownerAppUserDtos, String appType) {

        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            if (appType.equals(ownerAppUserDto.getAppType())) {
                return ownerAppUserDto;
            }
        }
        return null;
    }

    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + paramIn.getString("appId") + "&page=1&row=1", HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");

        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }

        return BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
