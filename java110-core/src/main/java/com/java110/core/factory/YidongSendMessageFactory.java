package com.java110.core.factory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.smsConfig.SmsConfigDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Random;

/**
 * 移动短信 对接
 * http://mas.10086.cn/login
 * http://112.35.10.201:1999/smsservice?wsdl
 * webservice 方式
 * Created by wuxw on 2019/3/23.
 */
public class YidongSendMessageFactory {


    private final static Logger logger = LoggerFactory.getLogger(YidongSendMessageFactory.class);


    private static OutRestTemplate outRestTemplate;


    public final static int DEFAULT_MESSAGE_CODE_LENGTH = 6;

    public final static String YIDONG_SMS_DOMAIN = "YIDONG_SMS";


    private final static String WEBSERVICE_BODY = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://server.webservice.service.mgw.mascloud.umpay.com/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <ser:sendTplSms>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>\n" +
            "\t         <![CDATA[\n" +
            "\t         \t\tREQUESTBODY\n" +
            "\t         ]]>\n" +
            "\t\t</arg0>\n" +
            "      </ser:sendTplSms>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";
    private final static String WEBSERVICE_BODY2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://server.webservice.service.mgw.mascloud.umpay.com/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <ser:sendSms>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>\n" +
            "\t\t<![CDATA[\n" +
            "\t\tREQUESTBODY\n" +
            "\t\t]]>\n" +
            "\t\t</arg0>\n" +
            "      </ser:sendSms>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

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
            result += (random.nextInt(9) + 1);
        }
        return result;
    }

//    public static void sendMessage(String tel, String code) {
//
//        String apId = MappingCache.getValue(YIDONG_SMS_DOMAIN, "apId");
//        String secretKey = MappingCache.getValue(YIDONG_SMS_DOMAIN, "secretKey");
//        String ecName = MappingCache.getValue(YIDONG_SMS_DOMAIN, "ecName");
//        String sign = MappingCache.getValue(YIDONG_SMS_DOMAIN, "sign");
//        String addSerial = MappingCache.getValue(YIDONG_SMS_DOMAIN, "addSerial");
//        String templateId = MappingCache.getValue(YIDONG_SMS_DOMAIN, "templateId");
//        String url = MappingCache.getValue(YIDONG_SMS_DOMAIN, "yidong_url");
//
//        String mac = getMac(ecName, apId, secretKey, templateId, tel, code, sign, addSerial);
//
//        //开始发送验证码
//        String reqParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<WsSubmitTempletReq>\n" +
//                "  <apId>" + apId + "</apId>\n" +
//                "  <secretKey>" + secretKey + "</secretKey>\n" +
//                "  <ecName>" + ecName + "</ecName>\n" +
//                "  <mobiles>\n" +
//                "    <string>" + tel + "</string>\n" +
//                "  </mobiles>\n" +
//                "  <params>\n" +
//                "    <string>" + code + "</string>\n" +
//                "  </params>\n" +
//                "  <sign>" + sign + "</sign>\n" +
//                "  <addSerial>" + addSerial + "</addSerial>\n" +
//                "  <mac>" + mac + "</mac>\n" +
//                "  <templateId>" + templateId + "</templateId>\n" +
//                "</WsSubmitTempletReq>";
//
//        reqParam = WEBSERVICE_BODY.replace("REQUESTBODY",reqParam);
//        logger.debug("请求移动公司请求报文：{}",reqParam);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Type","text/xml;charset=UTF-8");
//        outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate",OutRestTemplate.class);
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(reqParam, httpHeaders);
//        try {
//            ResponseEntity<String> responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
//            logger.debug("移动公司返回报文,{}",responseEntity);
//        }catch (HttpStatusCodeException e){
//            logger.error("调用异常",e);
//        }catch (Exception e){
//            logger.error("调用异常",e);
//        }
//
//    }

    public static void sendMessage(String tel, String code) {
        String apId = MappingCache.getValue(YIDONG_SMS_DOMAIN, "apId");
        String secretKey = MappingCache.getValue(YIDONG_SMS_DOMAIN, "secretKey");
        String ecName = MappingCache.getValue(YIDONG_SMS_DOMAIN, "ecName");
        String sign = MappingCache.getValue(YIDONG_SMS_DOMAIN, "sign");
        String addSerial = MappingCache.getValue(YIDONG_SMS_DOMAIN, "addSerial");
        String url = MappingCache.getValue(YIDONG_SMS_DOMAIN, "yidong_url");

        String param = "您的验证码是："+code+",验证码有效期5分钟";

        String mac = getMac(ecName, apId, secretKey, tel, param, sign, addSerial);

        //开始发送验证码
        String reqParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<WsSubmitTempletReq>\n" +
                "  <apId>" + apId + "</apId>\n" +
                "  <secretKey>" + secretKey + "</secretKey>\n" +
                "  <ecName>" + ecName + "</ecName>\n" +
                "  <mobiles>\n" +
                "    <string>" + tel + "</string>\n" +
                "  </mobiles>\n" +
                "    <content>" + param + "</content>\n" +
                "  <sign>" + sign + "</sign>\n" +
                "  <addSerial>" + addSerial + "</addSerial>\n" +
                "  <mac>" + mac + "</mac>\n" +
                "</WsSubmitTempletReq>";

        reqParam = WEBSERVICE_BODY2.replace("REQUESTBODY",reqParam);
        logger.debug("请求移动公司请求报文：{}",reqParam);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","text/xml;charset=UTF-8");
        outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate",OutRestTemplate.class);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(reqParam, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            logger.debug("移动公司返回报文,{}",responseEntity);
        }catch (HttpStatusCodeException e){
            logger.error("调用异常",e);
        }catch (Exception e){
            logger.error("调用异常",e);
        }


    }

    private static String getMac(String ecName, String apId, String secretKey, String templateId, String tel, String code, String sign, String addSerial) {

        String macStr = ecName + apId + secretKey + templateId + tel + code + sign + addSerial;
        return AuthenticationFactory.md5(macStr);
    }

    private static String getMac(String ecName, String apId, String secretKey, String tel, String content, String sign, String addSerial) {

        String macStr = ecName + apId + secretKey  + tel + content + sign + addSerial;
        return AuthenticationFactory.md5(macStr);
    }


    public static ResultVo sendOweFeeSms(String tel, Object param, SmsConfigDto smsConfigDto) {
        String apId = MappingCache.getValue(YIDONG_SMS_DOMAIN, "apId");
        String secretKey = MappingCache.getValue(YIDONG_SMS_DOMAIN, "secretKey");
        String ecName = MappingCache.getValue(YIDONG_SMS_DOMAIN, "ecName");
        String sign = MappingCache.getValue(YIDONG_SMS_DOMAIN, "sign");
        String addSerial = MappingCache.getValue(YIDONG_SMS_DOMAIN, "addSerial");
        String url = MappingCache.getValue(YIDONG_SMS_DOMAIN, "yidong_url");

        String mac = getMac(ecName, apId, secretKey, tel, param.toString(), sign, addSerial);

        //开始发送验证码
        String reqParam = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<WsSubmitTempletReq>\n" +
                "  <apId>" + apId + "</apId>\n" +
                "  <secretKey>" + secretKey + "</secretKey>\n" +
                "  <ecName>" + ecName + "</ecName>\n" +
                "  <mobiles>\n" +
                "    <string>" + tel + "</string>\n" +
                "  </mobiles>\n" +
                "    <content>" + param + "</content>\n" +
                "  <sign>" + sign + "</sign>\n" +
                "  <addSerial>" + addSerial + "</addSerial>\n" +
                "  <mac>" + mac + "</mac>\n" +
                "</WsSubmitTempletReq>";

        reqParam = WEBSERVICE_BODY2.replace("REQUESTBODY",reqParam);
        logger.debug("请求移动公司请求报文：{}",reqParam);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","text/xml;charset=UTF-8");
        outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate",OutRestTemplate.class);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(reqParam, httpHeaders);
        try {
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            logger.debug("移动公司返回报文,{}",responseEntity);
        }catch (HttpStatusCodeException e){
            logger.error("调用异常",e);
        }catch (Exception e){
            logger.error("调用异常",e);
        }


        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }
}
