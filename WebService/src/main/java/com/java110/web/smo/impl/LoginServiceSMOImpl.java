package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.CommonCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.ValidateCodeFactory;
import com.java110.web.smo.ILoginServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 登录信息实现类
 * Created by wuxw on 2019/3/20.
 */

@Service("loginServiceSMOImpl")
public class LoginServiceSMOImpl extends BaseServiceSMO implements ILoginServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceSMOImpl.class);


    private static char[] chs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int NUMBER_OF_CHS = 4;
    private static final int IMG_WIDTH = 65;
    private static final int IMG_HEIGHT = 25;
    private static Random r = new Random();
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录处理
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> doLogin(IPageData pd) {

        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(),"username","请求报文格式错误或未包含username信息");
        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());
        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("passwd")));
        responseEntity = this.callCenterService(restTemplate,pd,loginInfo.toJSONString(),ServiceConstant.SERVICE_API_URL+"/api/user.service.login",HttpMethod.POST);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            pd.setToken(JSONObject.parseObject(responseEntity.getBody()).getString("token"));
        }
        return responseEntity;
    }

    /**
     * 生成验证码
     * 参考地址：https://www.cnblogs.com/happyfans/p/4486010.html
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
            CommonCache.setValue(pd.getSessionId()+"_validateCode",verifyCode,CommonCache.defaultExpireTime);

        }catch (Exception e){
            logger.error("生成验证码失败，",e);
            verifyCodeImage = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return verifyCodeImage;
    }

    /**
     * 校验验证码
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> validate(IPageData pd){

        logger.debug("校验验证码参数:{}",pd.toString());
        ResponseEntity<String> verifyResult = null;
        Assert.jsonObjectHaveKey(pd.getReqData(),"code","请求报文中未包含 code节点"+pd.toString());

        String code = CommonCache.getValue(pd.getSessionId()+"_validateCode");

        if(JSONObject.parseObject(pd.getReqData()).getString("code").equals(code)){
            verifyResult = new ResponseEntity<>("成功", HttpStatus.OK);
        }else{
            verifyResult = new ResponseEntity<>("验证码错误", HttpStatus.INTERNAL_SERVER_ERROR);
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
