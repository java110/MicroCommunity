package com.java110.acct.payment.adapt.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PayUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信native支付
 */
@Service("wechatNativeQrcodePaymentFactory")
public class WechatNativeQrcodePaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(WechatNativeQrcodePaymentFactoryAdapt.class);


    public static final String wxPayUnifiedOrder = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    public static final String TRADE_TYPE_NATIVE = "NATIVE";

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;


    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;


    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {
        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);
        String paymentPoolId = reqJson.getString("paymentPoolId");

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");

        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/nativeWechat/992020011134400001/" + paymentPoolId;

        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                TRADE_TYPE_NATIVE,
                payAmount,
                smallWeChatDto,
                paymentPoolId,
                notifyUrl
        );

        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            resultMap.put("prepayId", resMap.get("prepay_id"));
            resultMap.put("codeUrl", resMap.get("code_url"));
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap + "===notifyUrl===" + notifyUrl);
        } else {
            resultMap.put("code", resMap.get("return_code"));
            resultMap.put("msg", resMap.get("return_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg") + "===code===" + resMap.get("return_code") + "===notifyUrl===" + notifyUrl);
        }
        return resultMap;
    }

    private Map<String, String> java110UnifieldOrder(String feeName, String orderNum,
                                                     String tradeType, double payAmount,
                                                     SmallWeChatDto smallWeChatDto,
                                                     String paymentPoolId,
                                                     String notifyUrl) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");


        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", smallWeChatDto.getAppId());
        paramMap.put("mch_id", mchId);
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("body", feeName);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", mchId);//起调小程序的商户号
        }
        paramMap.put("sign", PayUtil.createSign(paramMap, key));
//转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用支付统一下单接口" + xmlData);

        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                wxPayUnifiedOrder, xmlData, String.class);

        logger.debug("统一下单返回" + responseEntity);
//请求微信后台，获取预支付ID
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        doSaveOnlinePay(smallWeChatDto, "无", orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());
        return PayUtil.xmlStrToMap(responseEntity.getBody());
    }

    @Override
    public PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String resXml = "";
        String param = notifyPaymentOrderDto.getParam();
        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        try {
            Map<String, Object> map = PayUtil.getMapFromXML(param);
            logger.info("【小程序支付回调】 回调数据： \n" + map);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equalsIgnoreCase(returnCode)) {
                String returnmsg = (String) map.get("result_code");
                if ("SUCCESS".equals(returnmsg)) {
                    //更新数据
                    int result = confirmPayFee(map, paymentOrderDto, notifyPaymentOrderDto);
                    if (result > 0) {
                        //支付成功
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
                    }
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                    logger.info("支付失败:" + resXml);
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                logger.info("【订单支付失败】");
            }
        } catch (Exception e) {
            logger.error("通知失败", e);
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[鉴权失败]></return_msg>" + "</xml>";
        }
        paymentOrderDto.setResponseEntity(new ResponseEntity<String>(resXml, HttpStatus.OK));
        return paymentOrderDto;
    }

    public int confirmPayFee(Map<String, Object> map, PaymentOrderDto paymentOrderDto, NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String appId = "";
        //兼容 港币交易时 或者微信有时不会掉参数的问题
        if (map.containsKey("wId")) {
            String wId = map.get("wId").toString();
            wId = wId.replace(" ", "+");
            appId = WechatFactory.getAppId(wId);
        } else {
            appId = map.get("appid").toString();
        }
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        ResponseEntity<String> responseEntity = null;
        for (String key : map.keySet()) {
            if ("wId".equals(key)) {
                continue;
            }
            paramMap.put(key, map.get(key).toString());
        }

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(notifyPaymentOrderDto.getPaymentPoolId());
        paymentPoolValueDto.setCommunityId(notifyPaymentOrderDto.getCommunityId());
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        // String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");


        //String appId = WechatFactory.getAppId(wId);
//        JSONObject paramIn = new JSONObject();
//        paramIn.put("appId", appId);
//        paramIn.put("communityId", notifyPaymentOrderDto.getCommunityId());
//        SmallWeChatDto smallWeChatDto = getSmallWechat(paramIn);

        String sign = PayUtil.createSign(paramMap, key);

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String outTradeNo = map.get("out_trade_no").toString();
        paymentOrderDto.setOrderId(outTradeNo);
        paymentOrderDto.setTransactionId(map.get("transaction_id").toString());

        doUpdateOnlinePay(outTradeNo, OnlinePayDto.STATE_COMPILE, "支付成功");
        return 1;
    }


    private SmallWeChatDto getSmallWechat(JSONObject paramIn) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        smallWeChatDto.setAppId(paramIn.getString("appId"));
        smallWeChatDto.setPage(1);
        smallWeChatDto.setRow(1);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            smallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
//            smallWeChatDto.setMchId(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "mchId"));
//            smallWeChatDto.setPayPassword(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key"));
            smallWeChatDto.setObjId(paramIn.getString("communityId"));

            return smallWeChatDto;
        }

        return BeanConvertUtil.covertBean(smallWeChatDtos.get(0), SmallWeChatDto.class);
    }


    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId,
                                 String feeName, double money, String state, String message,
                                 String ppId) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setAppId(smallWeChatDto.getAppId());
        onlinePayPo.setMchId(smallWeChatDto.getMchId());
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOpenId(openId);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setPayId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        onlinePayPo.setPayName(feeName);
        onlinePayPo.setRefundFee("0");
        onlinePayPo.setState(state);
        onlinePayPo.setTotalFee(money + "");
        onlinePayPo.setTransactionId(orderId);
        onlinePayPo.setPaymentPoolId(ppId);
        onlinePayV1InnerServiceSMOImpl.saveOnlinePay(onlinePayPo);
    }

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

}
