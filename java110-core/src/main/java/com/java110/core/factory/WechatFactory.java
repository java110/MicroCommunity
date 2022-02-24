package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Synchronized;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @ClassName WechatFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/13 22:15
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
public class WechatFactory {

    private static Logger logger = LoggerFactory.getLogger(WechatFactory.class);

    private static final String password = "you are bad boy!";


    private static final String WECHAT = "WECHAT";

    private static final String SPLIT_STUB = "-";

    public static String formatText(String toUserName, String fromUserName, String content) {
        String str = "";
        str = String.format("<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%4$s]]></Content><FuncFlag>0</FuncFlag></xml>", new Object[]{
                fromUserName, toUserName, Long.valueOf((new Date()).getTime()), content
        });
        return str;
    }

    /**
     * 获取accessToken
     *
     * @param appId     公众号ID
     * @param appSecure
     * @return
     */
    @Java110Synchronized(value = "appId")
    public static String getAccessToken(String appId, String appSecure) {
        String accessToken = JWTCache.getValue(WECHAT + appId);
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
        String getAccessToken = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN,WechatConstant.GET_ACCESS_TOKEN_URL);
        if(StringUtil.isEmpty(getAccessToken)){
            getAccessToken = WechatConstant.GET_ACCESS_TOKEN;
        }
        String url = getAccessToken.replace("APPID", appId)
                .replace("SECRET", appSecure);
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        String response = outRestTemplate.getForObject(url, String.class);

        logger.debug("获取access_token 入参：" + url + " 返回参数" + response);

        JSONObject responseObj = JSONObject.parseObject(response);

        if (responseObj.containsKey("access_token")) {
            String accessToken = responseObj.getString("access_token");
            int expiresIn = responseObj.getInteger("expires_in");
            JWTCache.setValue(WECHAT + appId, accessToken, expiresIn - 200);
            return accessToken;
        }
        return "";
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
