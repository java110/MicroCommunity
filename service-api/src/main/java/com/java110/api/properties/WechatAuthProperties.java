package com.java110.api.properties;


import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "java110.auth.wechat")
@PropertySource("classpath:wechatAuth.properties")
public class WechatAuthProperties {

    //微信支付类型
//NATIVE--原生支付
//JSAPI--公众号支付-小程序支付
//MWEB--H5支付
//APP -- app支付
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_MWEB = "MWEB";
    public static final String TRADE_TYPE_APP = "APP";

    private String sessionHost;
    private String appId;
    private String secret;
    private String grantType;
    private String key;
    private String mchId;
    private String wxPayUnifiedOrder;
    private String wxNotifyUrl;
    private String rentingNotifyUrl;
    private String goodsNotifyUrl;
    private String oweFeeNotifyUrl;
    private String tempCarFeeNotifyUrl;


    private String wechatAppId;//微信公众号ID
    private String wechatAppSecret;//微信公众号秘钥

    private String sendMsgUrl;//微信公众号推送模板信息链接

    public String getSessionHost() {
        return sessionHost;
    }

    public void setSessionHost(String sessionHost) {
        this.sessionHost = sessionHost;
    }

    public String getAppId() {
        String appIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId");
        if (!StringUtil.isEmpty(appIdCache)) {
            return appIdCache;
        }
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {

        String appSecretCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret");
        if (!StringUtil.isEmpty(appSecretCache)) {
            return appSecretCache;
        }
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getKey() {
        String keyCache = MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key");
        if (!StringUtil.isEmpty(keyCache)) {
            return keyCache;
        }
        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getMchId() {
        String mchIdCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "mchId");
        if (!StringUtil.isEmpty(mchIdCache)) {
            return mchIdCache;
        }
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getWxPayUnifiedOrder() {
        return wxPayUnifiedOrder;
    }

    public void setWxPayUnifiedOrder(String wxPayUnifiedOrder) {
        this.wxPayUnifiedOrder = wxPayUnifiedOrder;
    }

    public String getWxNotifyUrl() {
        String wxNotifyUrlCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "wxNotifyUrl");
        if (!StringUtil.isEmpty(wxNotifyUrlCache)) {
            return UrlCache.getOwnerUrl()+wxNotifyUrlCache;
        }
        return wxNotifyUrl;
    }

    public void setWxNotifyUrl(String wxNotifyUrl) {
        this.wxNotifyUrl = wxNotifyUrl;
    }

    public String getWechatAppId() {
        String wechatAppIdCache = MappingCache.getValue(MappingConstant.MALL_WECHAT_DOMAIN, "wechatAppId");
        if (!StringUtil.isEmpty(wechatAppIdCache)) {
            return wechatAppIdCache;
        }
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWechatAppSecret() {
        String wechatAppSecretCache = MappingCache.getValue(MappingConstant.MALL_WECHAT_DOMAIN, "wechatAppSecret");
        if (!StringUtil.isEmpty(wechatAppSecretCache)) {
            return wechatAppSecretCache;
        }
        return wechatAppSecret;
    }

    public void setWechatAppSecret(String wechatAppSecret) {
        this.wechatAppSecret = wechatAppSecret;
    }

    public String getSendMsgUrl() {
        return sendMsgUrl;
    }

    public void setSendMsgUrl(String sendMsgUrl) {
        this.sendMsgUrl = sendMsgUrl;
    }

    public String getRentingNotifyUrl() {
        String rentingNotifyUrlCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "rentingNotifyUrl");
        if (!StringUtil.isEmpty(rentingNotifyUrlCache)) {
            return UrlCache.getMallAppUrl()+rentingNotifyUrlCache;
        }
        return rentingNotifyUrl;
    }

    public void setRentingNotifyUrl(String rentingNotifyUrl) {
        this.rentingNotifyUrl = rentingNotifyUrl;
    }

    public String getOweFeeNotifyUrl() {
        String oweFeeNotifyUrlCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "oweFeeNotifyUrl");
        if (!StringUtil.isEmpty(oweFeeNotifyUrlCache)) {
            return UrlCache.getOwnerUrl()+oweFeeNotifyUrlCache;
        }
        return oweFeeNotifyUrl;
    }

    public void setOweFeeNotifyUrl(String oweFeeNotifyUrl) {
        this.oweFeeNotifyUrl = oweFeeNotifyUrl;
    }

    public String getGoodsNotifyUrl() {
        String goodsNotifyUrlCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "goodsNotifyUrl");
        if (!StringUtil.isEmpty(goodsNotifyUrlCache)) {
            return UrlCache.getMallAppUrl()+goodsNotifyUrlCache;
        }
        return goodsNotifyUrl;
    }

    public void setGoodsNotifyUrl(String goodsNotifyUrl) {
        this.goodsNotifyUrl = goodsNotifyUrl;
    }

    public String getTempCarFeeNotifyUrl() {
        String tempCarFeeNotifyUrlCache = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "tempCarFeeNotifyUrl");
        if (!StringUtil.isEmpty(tempCarFeeNotifyUrlCache)) {
            return UrlCache.getOwnerUrl()+ tempCarFeeNotifyUrlCache;
        }
        return tempCarFeeNotifyUrl;
    }

    public void setTempCarFeeNotifyUrl(String tempCarFeeNotifyUrl) {
        this.tempCarFeeNotifyUrl = tempCarFeeNotifyUrl;
    }
}
