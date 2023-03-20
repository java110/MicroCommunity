package com.java110.common.charge.factory.dingding;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Synchronized;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import org.slf4j.Logger;

public class DingdingChargeUtils {

    private static Logger logger = LoggerFactory.getLogger(DingdingChargeUtils.class);

    public static final String DING_DING_CHARGE_MACHINE = "DING_DING_CHARGE_MACHINE_";

    public static final String DING_DING_DOMAIN = "DING_DING_CHARGE";


    public static final String DING_DING_APP_ID = "APP_ID";
    public static final String DING_DING_APP_SECURE = "APP_SECURE";

    public static final String URL = "https://open.api.ddzn.tech";

    private static final String TOKEN_URL = URL+"/token?appid=APPID&timestamp=TIMESTAMP&sign=SIGN";


    /**
     * 请求充电桩 接口
     *
     * @param url
     * @param body
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String execute(String url, String body, HttpMethod httpMethod) throws Exception {
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "
                + getAccessToken(MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_ID),
                MappingCache.getValue(DING_DING_DOMAIN, DING_DING_APP_SECURE)));
        httpHeaders.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
        ResponseEntity<String> response = null;
        try {
            response = outRestTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) {
            logger.error("请求异常", e.getResponseBodyAsString());
            return e.getResponseBodyAsString();
        }

        return response.getBody();
    }

    /**
     * 获取accessToken
     *
     * @param appId     公众号ID
     * @param appSecure
     * @return
     */
    @Java110Synchronized(value = "appId")
    public static String getAccessToken(String appId, String appSecure) throws Exception {
        String accessToken = JWTCache.getValue(DING_DING_CHARGE_MACHINE + appId);
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
    private static String refreshAccessToken(String appId, String appSecure) throws Exception {
        String getAccessToken = TOKEN_URL;
        long timestamp = DateUtil.getCurrentDate().getTime();
        String url = getAccessToken.replace("APPID", appId)
                .replace("TIMESTAMP", timestamp + "")
                .replace("SIGN", getSign(appId, appSecure, timestamp));
        RestTemplate outRestTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        String response = outRestTemplate.getForObject(url, String.class);

        logger.debug("获取access_token 入参：" + url + " 返回参数" + response);

        JSONObject responseObj = JSONObject.parseObject(response);

        if (200 != responseObj.getIntValue("code")) {
            throw new IllegalArgumentException(responseObj.getString("msg"));
        }

        responseObj = responseObj.getJSONObject("data");

        if (responseObj.containsKey("token")) {
            String accessToken = responseObj.getString("token");
            long expiresIn = responseObj.getLongValue("expireTime");
            timestamp = DateUtil.getCurrentDate().getTime();
            JWTCache.setValue(DING_DING_CHARGE_MACHINE + appId, accessToken, (int) ((expiresIn - timestamp)/1000 - 200));
            return accessToken;
        }
        return "";
    }

    private static String getSign(String appId, String secret, long timestamp) throws Exception {
        String data = "appid=" + appId + "&timestamp=" + timestamp;
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        String result =
                Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        return result;
    }
}
