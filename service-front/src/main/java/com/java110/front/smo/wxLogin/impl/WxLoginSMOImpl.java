package com.java110.front.smo.wxLogin.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.wxLogin.IWxLoginSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wx登录
 */
@Service("wxLoginSMOImpl")
public class WxLoginSMOImpl extends AppAbstractComponentSMO implements IWxLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(WxLoginSMOImpl.class);

    @Autowired
    private RestTemplate outRestTemplate;

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
        String response = outRestTemplate.getForObject(
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

        //responseEntity = super.getUserInfoByOpenId(pd, restTemplate, openId);
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setOpenId(openId);
        List<OwnerAppUserDto> ownerAppUserDtos = CallApiServiceFactory.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);
        JSONObject paramOut = new JSONObject();
        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            //将openId放到redis 缓存，给前段下发临时票据
            paramOut.put("openId", openId);
            paramOut.put("msg", "还没有注册请先注册");
            responseEntity = new ResponseEntity<String>(paramOut.toJSONString(), HttpStatus.UNAUTHORIZED);

            return responseEntity;
        }
        JSONObject userInfo = paramIn.getJSONObject("userInfo");
        userInfo.putAll(BeanConvertUtil.beanCovertMap(ownerAppUserDtos.get(0)));
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
