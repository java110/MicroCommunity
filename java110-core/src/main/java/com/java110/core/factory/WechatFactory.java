package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

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
        String url = WechatConstant.GET_ACCESS_TOKEN.replace("APPID", appId)
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
        return AuthenticationFactory.decrypt(password, wId);
    }

}
