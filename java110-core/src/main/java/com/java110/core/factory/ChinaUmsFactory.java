package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Synchronized;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName 银联工具类
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/13 22:15
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
public class ChinaUmsFactory {

    private static Logger logger = LoggerFactory.getLogger(ChinaUmsFactory.class);

    private static final String password = "you are bad boy!";


    private static final String CHINA_UMS = "CHINA_UMS";
    public static final String CHINA_UMS_DOMAIN = "CHINA_UMS";
    private static final String GET_ACCESS_TOKEN = "https://api-mop.chinaums.com/v1/token/access";


    private static final String SPLIT_STUB = "-";

    /**
     * 获取accessToken
     *
     * @return
     */
    @Java110Synchronized(value = "appId")
    public static String getAccessToken(SmallWeChatDto smallWeChatDto) {
        String appId = smallWeChatDto.getRemarks().split("::")[0];
        String appSecure = smallWeChatDto.getRemarks().split("::")[1];
        String accessToken = JWTCache.getValue(CHINA_UMS + appId);
        if (StringUtil.isEmpty(accessToken)) {
            return refreshAccessToken(appId, appSecure);
        }
        return accessToken;
    }

    /**
     * 刷新 access_token
     *
     * @param appId     应用ID
     * @param appSecure 应用秘钥
     * @return
     */
    private static String refreshAccessToken(String appId, String appSecure) {
        String url = MappingCache.getRemark(CHINA_UMS_DOMAIN, WechatConstant.GET_ACCESS_TOKEN_URL);
        if (StringUtil.isEmpty(url)) {
            url = GET_ACCESS_TOKEN;
        }

        JSONObject paramMap = new JSONObject();
        paramMap.put("appId", appId);
        paramMap.put("timestamp", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        paramMap.put("nonce", PayUtil.makeUUID(32));
        paramMap.put("signMethod", "SHA256");
        paramMap.put("signature", getSignature(paramMap, appSecure));


        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        logger.debug("获取access_token 入参：" + url + " 请求参数" + paramMap.toJSONString());

        //ResponseEntity<String> response = outRestTemplate.postForEntity(url, paramMap.toJSONString(), String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(paramMap.toJSONString(), headers);
        ResponseEntity<String> response = outRestTemplate.exchange(
                url, HttpMethod.POST, httpEntity, String.class);

        logger.debug("获取access_token 入参：" + url + " 返回参数" + response);

        JSONObject responseObj = JSONObject.parseObject(response.getBody());

        if (!"0000".equals(responseObj.getString("errCode"))) {
            throw new IllegalArgumentException("获取签名失败");
        }

        if (responseObj.containsKey("accessToken")) {
            String accessToken = responseObj.getString("accessToken");
            int expiresIn = responseObj.getInteger("expiresIn");
            JWTCache.setValue(CHINA_UMS + appId, accessToken, expiresIn - 200);
            return accessToken;
        }
        return "";
    }

    public static String getSignature(JSONObject paramMap, String appSecure) {
        MessageDigest messageDigest;
        String str = paramMap.getString("appId") + paramMap.getString("timestamp") + paramMap.getString("nonce") + appSecure;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    /**
     * 获取微信页面ID
     *
     * @param appId
     * @return
     */
    public static String getWId(String appId) {
        return AuthenticationFactory.encrypt(password, appId);
    }

    /**
     * 获取微信AppId
     *
     * @param wId
     * @return
     */
    public static String getAppId(String wId) {
        wId = wId.replace(" ", "+");
        return AuthenticationFactory.decrypt(password, wId);
    }


    public static String getPhoneNumberBeanS5(String decryptData, String key, String iv) {
        /*
         *这里你没必要非按照我的方式写，下面打代码主要是在一个自己的类中 放上decrypts5这个解密工具，工具在下方有代码
         */
        String resultMessage = decryptS5(decryptData, "UTF-8", key, iv);
        return resultMessage;
    }

    public static String decryptS5(String sSrc, String encodingFormat, String sKey, String ivParameter) {
        try {
            Base64 base64 = new Base64();
            byte[] raw = base64.decode(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(base64.decode(ivParameter));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] myendicod = base64.decode(sSrc);
            byte[] original = cipher.doFinal(myendicod);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}
