package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/

/**
 * Created by wuxw on 2019/3/23.
 */
public class TencentSendMessageFactory {

    private final static String TENCENT_SMS_DOMAIN = "TENCENT_SMS";


    private final static Logger logger = LoggerFactory.getLogger(TencentSendMessageFactory.class);


    public final static int DEFAULT_MESSAGE_CODE_LENGTH = 6;

    // private RestTemplate restTemplateNoLoadBalanced;


    /**
     * 生成6位短信码
     *
     * @return
     */
    public static String generateMessageCode() {
        return generateMessageCode(DEFAULT_MESSAGE_CODE_LENGTH);
    }

    /**
     * 生成验证码
     *
     * @param limit 位数
     * @return
     */
    public static String generateMessageCode(int limit) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < limit; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    public static void sendMessage(String tel, String code) {

        //开始发送验证码
        logger.debug("发送号码为{}，短信码为{}", tel, code);
//        String url = "https://sms.tencentcloudapi.com/?Action=SendSms" +
//                "&PhoneNumberSet.0=+86" + tel +
//                "&TemplateID=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "TemplateID") +
//                "&Sign=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "Sign") +
//                "&TemplateParamSet.0=" + code +
//                "&SmsSdkAppid=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "SmsSdkAppid") +
//                "&Version=2019-07-11";
//        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);

        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey，见《创建secretId和secretKey》小节
        Credential cred = new Credential(MappingCache.getValue(TENCENT_SMS_DOMAIN, "secretId"),
                MappingCache.getValue(TENCENT_SMS_DOMAIN, "secretKey"));
        // 实例化要请求产品(以cvm为例)的client对象
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
        SmsClient smsClient = new SmsClient(cred, MappingCache.getValue(TENCENT_SMS_DOMAIN, "region"));//第二个ap-chongqing 填产品所在的区
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setSmsSdkAppid(MappingCache.getValue(TENCENT_SMS_DOMAIN, "SmsSdkAppid"));//appId ,见《创建应用》小节
        String[] phones = {"+86" + tel};  //发送短信的目标手机号，可填多个。
        sendSmsRequest.setPhoneNumberSet(phones);
        sendSmsRequest.setTemplateID(MappingCache.getValue(TENCENT_SMS_DOMAIN, "TemplateID"));  //模版id,见《创建短信签名和模版》小节
        String[] templateParam = {code};//模版参数，从前往后对应的是模版的{1}、{2}等,见《创建短信签名和模版》小节
        sendSmsRequest.setTemplateParamSet(templateParam);
        sendSmsRequest.setSign(MappingCache.getValue(TENCENT_SMS_DOMAIN, "Sign")); //签名内容，不是填签名id,见《创建短信签名和模版》小节
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = smsClient.SendSms(sendSmsRequest); //发送短信
        } catch (TencentCloudSDKException e) {
            logger.error("发送短信失败", e);
        }

        logger.debug("腾讯短信验证码发送，请求报文" + sendSmsRequest.toString() + ",返回日志" + sendSmsResponse != null ? sendSmsResponse.toString() : "");


    }

//    private static RestTemplate getRestTemplate() {
//        return ApplicationContextFactory.getBean("restTemplateNoLoadBalanced", RestTemplate.class);
//    }
}
