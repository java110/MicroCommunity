package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AliSendMessageFactory;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.SendSmsFactory;
import com.java110.api.smo.IRegisterServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
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
 * 用户注册业务处理类
 * <p>
 * Created by wuxw on 2019/3/23.
 */
@Service("registerServiceSMOImpl")
public class RegisterServiceSMOImpl extends DefaultAbstractComponentSMO implements IRegisterServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(RegisterServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> doRegister(IPageData pd) {
        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(), "username", "请求报文格式错误或未包含用户名信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "passwd", "请求报文格式错误或未包含密码信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "repasswd", "请求报文格式错误或未包含确认密码信息");
        Assert.jsonObjectHaveKey(pd.getReqData(), "tel", "请求报文格式错误或未包含手机信息信息");
        JSONObject registerInfo = JSONObject.parseObject(pd.getReqData());

        //校验密码
        if (!registerInfo.getString("passwd").equals(registerInfo.getString("repasswd"))) {
            responseEntity = new ResponseEntity<String>("密码和确认密码不一致", HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }

        //调用 手机验证码
        responseEntity = this.invokeComponent("validate-tel", "validate", pd);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        responseEntity = this.checkNameAndTelExists(pd, registerInfo.getString("username"), "");
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        responseEntity = this.checkNameAndTelExists(pd, "", registerInfo.getString("tel"));
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }


        registerInfo.put("passwd", AuthenticationFactory.passwdMd5(registerInfo.getString("passwd")));

        registerInfo.put("name", registerInfo.getString("username"));
        registerInfo.put("password", registerInfo.getString("passwd"));
        responseEntity = this.callCenterService(restTemplate, pd, registerInfo.toJSONString(), "user.service.register", HttpMethod.POST);
        return responseEntity;
    }

    private ResponseEntity<String> checkNameAndTelExists(IPageData pd, String name, String tel) {
        ResponseEntity<String> responseEntity = null;
        //校验用户名或手机是否存在
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "check.hasUser.byNameOrTel?name=" + name + "&tel=" + tel,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("调用后端服务异常：{}", responseEntity);
            return new ResponseEntity<String>("调用中心服务异常", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody(), "userCount", "调用中心服务异常，报文中未包含userCount节点");

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());

        if (userInfo.getIntValue("userCount") > 0) {
            return new ResponseEntity<String>(StringUtil.isNullOrNone(name) ? "手机号已占用" : "用户名已占用", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseEntity = new ResponseEntity<>("成功", HttpStatus.OK);

        return responseEntity;

    }

    /**
     * 发送验证码
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> sendTelMessageCode(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "tel", "请求报文格式错误或未包含手机号信息");
        JSONObject telInfo = JSONObject.parseObject(pd.getReqData());

        String oldCode = CommonCache.getValue(telInfo.getString("tel") + "_validateTel_resend");
        ResponseEntity<String> sendMessageResult = null;
        String verifyStr = "";
        if(!StringUtil.isNullOrNone(oldCode)){
            verifyStr = "请稍后重试";
            sendMessageResult = new ResponseEntity<>(verifyStr, HttpStatus.OK);
            return sendMessageResult;
        }


        String verifyCode = AliSendMessageFactory.generateMessageCode();

        verifyStr = "演示环境验证码:" + verifyCode;
        try {
            if ("ON".equals(MappingCache.getValue(MappingConstant.SMS_DOMAIN,SendSmsFactory.SMS_SEND_SWITCH))) {
                //开始发送验证码
                //AliSendMessageFactory.sendMessage(telInfo.getString("tel"), verifyCode);

                //TencentSendMessageFactory.sendMessage(telInfo.getString("tel"), verifyCode);
                SendSmsFactory.sendSms(telInfo.getString("tel"), verifyCode);

                verifyStr = "验证码已下发至您的手机!";
            }
            //将验证码存入Redis中
            CommonCache.setValue(telInfo.getString("tel") + "_validateTel", verifyCode.toLowerCase(), CommonCache.defaultExpireTime);
            //将验证码存入Redis中
            CommonCache.setValue(telInfo.getString("tel") + "_validateTel_resend", verifyCode.toLowerCase(), CommonCache.RESEND_DEFAULT_EXPIRETIME);

            sendMessageResult = new ResponseEntity<>(verifyStr, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("生成验证码失败，", e);
            sendMessageResult = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return sendMessageResult;

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
        Assert.jsonObjectHaveKey(pd.getReqData(), "messageCode", "请求报文中未包含 验证码" + pd.toString());
        Assert.jsonObjectHaveKey(pd.getReqData(), "tel", "请求报文中未包含 手机号" + pd.toString());
        JSONObject telInfo = JSONObject.parseObject(pd.getReqData());

        String code = CommonCache.getValue(telInfo.getString("tel") + "_validateTel");

        if (telInfo.getString("messageCode").toLowerCase().equals(code)) {
            verifyResult = new ResponseEntity<>("成功", HttpStatus.OK);
        } else {
            pd.setToken("");
            verifyResult = new ResponseEntity<>("验证码错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return verifyResult;
    }

    /**
     * 加载地区
     *
     * @param pd 页面请求对象
     * @return
     */
    public ResponseEntity<String> loadArea(IPageData pd) {
        ResponseEntity responseEntity = null;
        JSONObject paramObj = JSONObject.parseObject(pd.getReqData());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "area.listAreas?" + super.mapToUrlParam(paramObj),
                HttpMethod.GET);

        return responseEntity;
    }
}
