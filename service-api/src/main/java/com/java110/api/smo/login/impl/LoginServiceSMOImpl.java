package com.java110.api.smo.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.cache.Java110RedisConfig;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.ValidateCodeFactory;
import com.java110.api.smo.login.ILoginServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 登录信息实现类
 * Created by wuxw on 2019/3/20.
 */

@Service("loginServiceSMOImpl")
public class LoginServiceSMOImpl extends DefaultAbstractComponentSMO implements ILoginServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录处理
     *
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> doLogin(IPageData pd) {

        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(), "username", "请求报文格式错误或未包含username信息");
        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());

        //调用 验证码组件验证码是否正确
        responseEntity = this.invokeComponent("validate-code", "validate", pd);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("passwd")));
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), "login.pcUserLogin", HttpMethod.POST);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());
            pd.setToken(userInfo.getString("token"));

            //清理缓存

            clearUserCache(userInfo);
        }
        return responseEntity;
    }

    /**
     * 清理用户缓存
     *
     * @param userInfo
     */
    private void clearUserCache(JSONObject userInfo) {
        //员工商户缓存 getStoreInfo
        String storeId = "";

        String storeInfo = CommonCache.getValue("getStoreInfo" + Java110RedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
        if(!StringUtil.isEmpty(storeInfo)){
            CommonCache.removeValue("getStoreInfo" + Java110RedisConfig.GET_STORE_INFO_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
            JSONObject storeObj = JSONObject.parseObject(storeInfo);
            storeId = storeObj.getJSONObject("msg").getString("storeId");
            CommonCache.removeValue("getStoreEnterCommunitys" + Java110RedisConfig.GET_STORE_ENTER_COMMUNITYS_EXPIRE_TIME_KEY + "::" + storeId);
        }
        //员工权限
        CommonCache.removeValue("getUserPrivileges" + Java110RedisConfig.DEFAULT_EXPIRE_TIME_KEY + "::" + userInfo.getString("userId"));
    }

    /**
     * 生成验证码
     * 参考地址：https://www.cnblogs.com/happyfans/p/4486010.html
     *
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> generateValidateCode(IPageData pd) {
        int w = 200, h = 80;
        String verifyCode = ValidateCodeFactory.generateVerifyCode(4);
        ResponseEntity<String> verifyCodeImage = null;
        try {
            verifyCodeImage = new ResponseEntity<>(ValidateCodeFactory.outputImage(200, 80, verifyCode), HttpStatus.OK);

            //将验证码存入Redis中
            CommonCache.setValue(pd.getSessionId() + "_validateCode", verifyCode.toLowerCase(), CommonCache.defaultExpireTime);

        } catch (Exception e) {
            logger.error("生成验证码失败，", e);
            verifyCodeImage = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return verifyCodeImage;
    }

    /**
     * 校验验证码
     *
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> validate(IPageData pd) {

        logger.debug("校验验证码参数:{}", pd.toString());
        ResponseEntity<String> verifyResult = null;
        Assert.jsonObjectHaveKey(pd.getReqData(), "validateCode", "请求报文中未包含 validateCode节点" + pd.toString());

        String code = CommonCache.getValue(pd.getSessionId() + "_validateCode");

        if (JSONObject.parseObject(pd.getReqData()).getString("validateCode").toLowerCase().equals(code)) {
            verifyResult = new ResponseEntity<>("成功", HttpStatus.OK);
        } else {
            pd.setToken("");
            verifyResult = new ResponseEntity<>("验证码错误或已失效", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return verifyResult;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
