package com.java110.core.factory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AliSendMessageFactory {


    private final static Logger logger = LoggerFactory.getLogger(AliSendMessageFactory.class);


    public final static int DEFAULT_MESSAGE_CODE_LENGTH = 6;


    /**
     * 生成6位短信码
     * @return
     */
    public static String generateMessageCode(){
        return generateMessageCode(DEFAULT_MESSAGE_CODE_LENGTH);
    }

    /**
     * 生成验证码
     * @param limit 位数
     * @return
     */
    public static String generateMessageCode(int limit){
        Random random = new Random();
        String result="";
        for (int i=0;i<limit;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }

    public static void sendMessage(String tel,String code) {

        //开始发送验证码
        logger.debug("发送号码为{}，短信码为{}",tel,code);
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", tel);
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.debug("发送验证码信息：{}",response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
