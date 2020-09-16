package com.java110.front.smo;

import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AppAbstractComponentSMO extends AbstractComponentSMO {

    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);
    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private RestTemplate restTemplate;

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";
    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";


    /**
     * 调用中心服务
     *
     * @return
     */
    @Override
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {

        Assert.notNull(pd.getAppId(), "请求头中未包含应用信息");
        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), pd.getAppId());
        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), StringUtil.isEmpty(pd.getUserId()) ? CommonConstant.ORDER_DEFAULT_USER_ID : pd.getUserId());
        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), pd.getTransactionId());
        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), pd.getRequestTime());
        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
        //logger.debug("请求中心服务信息，{}", httpEntity);
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
            return responseEntity;
        }

    }

    /**
     * 预下单
     *
     * @param orderNum
     * @param money
     * @param openId
     * @return
     * @throws Exception
     */
    protected Map<String, String> java110Payment(RestTemplate outRestTemplate,
                                                 String feeName, String tradeType,
                                                 String orderNum, double money,
                                                 String openId, SmallWeChatDto smallWeChatDto) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
//生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元

        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue("HC_ENV"), money);
//添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = this.java110UnifieldOrder(outRestTemplate, feeName, orderNum, tradeType, payAmount, openId, smallWeChatDto);
        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            if (WechatAuthProperties.TRADE_TYPE_JSAPI.equals(tradeType)) {

                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("package", "prepay_id=" + resMap.get("prepay_id"));
                resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (WechatAuthProperties.TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.get("prepay_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (WechatAuthProperties.TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.get("prepay_id"));
                resultMap.put("codeUrl", resMap.get("code_url"));
            }
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap);
        } else {
            resultMap.put("code", resMap.get("return_code"));
            resultMap.put("msg", resMap.get("return_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg"));
        }
        return resultMap;
    }

    /**
     * 小程序支付统一下单
     */
    private Map<String, String> java110UnifieldOrder(RestTemplate outRestTemplate, String feeName, String orderNum,
                                                     String tradeType, double payAmount, String openid,
                                                     SmallWeChatDto smallWeChatDto) throws Exception {

        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", smallWeChatDto.getAppId());
        paramMap.put("mch_id", smallWeChatDto.getMchId());
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("body", systemName + feeName);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", wechatAuthProperties.getWxNotifyUrl() + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid", openid);

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", smallWeChatDto.getMchId());//起调小程序的商户号
            paramMap.put("sub_openid", openid);
            paramMap.remove("openid");
        }
        paramMap.put("sign", PayUtil.createSign(paramMap, smallWeChatDto.getPayPassword()));
//转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用支付统一下单接口" + xmlData);

        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                wechatAuthProperties.getWxPayUnifiedOrder(), xmlData, String.class);

        logger.debug("统一下单返回" + responseEntity);
//请求微信后台，获取预支付ID
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        return PayUtil.xmlStrToMap(responseEntity.getBody());
    }

}
