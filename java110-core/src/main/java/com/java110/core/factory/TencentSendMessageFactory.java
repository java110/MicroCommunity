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

    private RestTemplate restTemplateNoLoadBalanced;


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
        String url = "https://sms.tencentcloudapi.com/?Action=SendSms" +
                "&PhoneNumberSet.0=+86" + tel +
                "&TemplateID=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "TemplateID") +
                "&Sign=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "Sign") +
                "&TemplateParamSet.0=" + code +
                "&SmsSdkAppid=" + MappingCache.getValue(TENCENT_SMS_DOMAIN, "SmsSdkAppid") +
                "&Version=2019-07-11";
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);

        logger.debug("腾讯短信验证码发送，请求报文" + url + ",返回日志" + responseEntity);
    }

    private static RestTemplate getRestTemplate() {
        return ApplicationContextFactory.getBean("restTemplateNoLoadBalanced", RestTemplate.class);
    }
}
