package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.CommonCache;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AliSendMessageFactory;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.ValidateCodeFactory;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IRegisterServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * 用户注册业务处理类
 *
 * Created by wuxw on 2019/3/23.
 */
@Service("registerServiceSMOImpl")
public class RegisterServiceSMOImpl extends BaseComponentSMO implements IRegisterServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(RegisterServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;
    @Override
    public ResponseEntity<String> doRegister(IPageData pd) {
        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(),"username","请求报文格式错误或未包含用户名信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"passwd","请求报文格式错误或未包含密码信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"repasswd","请求报文格式错误或未包含确认密码信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"tel","请求报文格式错误或未包含手机信息信息");
        JSONObject registerInfo = JSONObject.parseObject(pd.getReqData());

        //校验密码
        if(!registerInfo.getString("passwd").equals(registerInfo.getString("repasswd"))){
            responseEntity = new ResponseEntity<String>("密码和确认密码不一致",HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }

        //调用 手机验证码
        responseEntity = this.invokeComponent("validate-tel","validate",pd);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }

        registerInfo.put("passwd", AuthenticationFactory.passwdMd5(registerInfo.getString("passwd")));
        responseEntity = this.callCenterService(restTemplate,pd,registerInfo.toJSONString(), ServiceConstant.SERVICE_API_URL+"/api/user.service.register", HttpMethod.POST);
        return responseEntity;
    }

    /**
     * 发送验证码
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> sendTelMessageCode(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(),"tel","请求报文格式错误或未包含手机号信息");
        JSONObject telInfo = JSONObject.parseObject(pd.getReqData());

        String verifyCode = AliSendMessageFactory.generateMessageCode();
        ResponseEntity<String> sendMessageResult = null;
        try {
            //开始发送验证码
            AliSendMessageFactory.sendMessage(telInfo.getString("tel"),verifyCode);

            //将验证码存入Redis中
            CommonCache.setValue(telInfo.getString("tel")+"_validateTel",verifyCode.toLowerCase(),CommonCache.defaultExpireTime);

            sendMessageResult = new ResponseEntity<>("成功", HttpStatus.OK);

        }catch (Exception e){
            logger.error("生成验证码失败，",e);
            sendMessageResult = new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return sendMessageResult;

    }
}
