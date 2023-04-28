package com.java110.acct.payment.adapt.pingan;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * 平安支付 第三方支付对接
 *
 * 使用请先到 平安支付 获取相关配置信息
 *
 * https://www.pingan.com/
 *
 * @desc add by 吴学文 15:33
 */
@Service("panganPaymentFactory")
public class PinganPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(PinganPaymentFactoryAdapt.class);


    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_MWEB = "MWEB";
    public static final String TRADE_TYPE_APP = "APP";

    public static final String PAY_UNIFIED_ORDER_URL = "https://aipay.pingan.com/aggregatePay/wxPreCreate";


    private static final String VERSION = "1.0";


    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;


    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {

        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);


        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String tradeType = reqJson.getString("tradeType");
        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/wechat/992020011134400001/"+smallWeChatDto.getObjId();

        String openId = reqJson.getString("openId");

        if(StringUtil.isEmpty(openId)) {
            String appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
            if (AppDto.WECHAT_OWNER_APP_ID.equals(appId)) {
                appType = OwnerAppUserDto.APP_TYPE_WECHAT;
            } else if (AppDto.WECHAT_MINA_OWNER_APP_ID.equals(appId)) {
                appType = OwnerAppUserDto.APP_TYPE_WECHAT_MINA;
            } else {
                appType = OwnerAppUserDto.APP_TYPE_APP;
            }

            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setUserId(userId);
            ownerAppUserDto.setAppType(appType);
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

            Assert.listOnlyOne(ownerAppUserDtos, "未找到开放账号信息");
            openId = ownerAppUserDtos.get(0).getOpenId();
        }


        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        JSONObject resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                tradeType,
                payAmount,
                openId,
                smallWeChatDto,
                notifyUrl
        );

        if ("000000".equals(resMap.getString("result_code"))) {
            if (TRADE_TYPE_JSAPI.equals(tradeType)) {
                resultMap.putAll(JSONObject.toJavaObject(JSONObject.parseObject(resMap.getString("reserved_pay_info")), Map.class));
                resultMap.put("sign", resultMap.get("paySign"));
            } else if (TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.getString("session_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, smallWeChatDto.getPayPassword()));
            } else if (TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.getString("session_id"));
                resultMap.put("codeUrl", resMap.getString("qr_code"));
            }
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap);
        } else {
            resultMap.put("code", resMap.getString("result_code"));
            resultMap.put("msg", resMap.getString("result_msg"));
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.get("return_msg"));
        }
        return resultMap;
    }


    private JSONObject java110UnifieldOrder(String feeName, String orderNum,
                                            String tradeType, double payAmount, String openid,
                                            SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);
        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }
        String orderPre = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "FUIOU_ORDER_PRE");

        JSONObject paramMap = new JSONObject();
        paramMap.put("version", VERSION);
        paramMap.put("mchnt_cd", smallWeChatDto.getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("random_str", PayUtil.makeUUID(32));
        paramMap.put("order_amt", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("mchnt_order_no", orderPre + orderNum);
        paramMap.put("txn_begin_ts", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT));
        paramMap.put("goods_des", feeName);
        paramMap.put("term_id", "abcdefgh");
        paramMap.put("term_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        paramMap.put("sub_openid", openid);
        paramMap.put("sub_appid", smallWeChatDto.getAppId());

        paramMap.put("sign", createSign(paramMap, smallWeChatDto.getPayPassword()));

        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(paramMap.toJSONString(), headers);
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(
                PAY_UNIFIED_ORDER_URL, HttpMethod.POST, httpEntity, String.class);

        logger.debug("统一下单返回" + responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        return JSONObject.parseObject(responseEntity.getBody());
    }


    @Override
    public PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String param = notifyPaymentOrderDto.getParam();
        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();

        JSONObject resJson = new JSONObject();
        resJson.put("result_code", "010002");
        resJson.put("result_msg", "失败");
        try {
            JSONObject map = JSONObject.parseObject(param);
            logger.info("【富友支付回调】 回调数据： \n" + map);
            String resultCode = map.getString("result_code");
            if ("000000".equals(resultCode)) {
                //更新数据

                int result = confirmPayFee(map, paymentOrderDto,notifyPaymentOrderDto);
                if (result > 0) {
                    //支付成功
                    resJson.put("result_code", "000000");
                    resJson.put("result_msg", "成功");
                }
            }
        } catch (Exception e) {
            logger.error("通知失败", e);
            resJson.put("result_msg", "鉴权失败");
        }
        paymentOrderDto.setResponseEntity(new ResponseEntity<String>(resJson.toJSONString(), HttpStatus.OK));
        return paymentOrderDto;
    }

    public int confirmPayFee(JSONObject map, PaymentOrderDto paymentOrderDto,NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String appId = null;
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
        //String appId = WechatFactory.getAppId(wId);
        JSONObject paramIn = new JSONObject();
        paramIn.put("appId", appId);
        paramIn.put("communityId",notifyPaymentOrderDto.getCommunityId());
        SmallWeChatDto smallWeChatDto = getSmallWechat(paramIn);

        String sign = createSign(map, smallWeChatDto.getPayPassword());

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String outTradeNo = map.get("mchnt_order_no").toString();
        paymentOrderDto.setOrderId(outTradeNo);
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
            smallWeChatDto.setMchId(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "mchId"));
            smallWeChatDto.setPayPassword(MappingCache.getValue(MappingConstant.WECHAT_STORE_DOMAIN, "key"));
            smallWeChatDto.setObjId(paramIn.getString("communityId"));

            return smallWeChatDto;
        }

        return BeanConvertUtil.covertBean(smallWeChatDtos.get(0), SmallWeChatDto.class);
    }

    /**
     * 富友 生成sign 方法
     *
     * @param paramMap
     * @param payPassword
     * @return
     */
    private String createSign(JSONObject paramMap, String payPassword) {
        String str = paramMap.getString("mchnt_cd") + "|"
                + paramMap.getString("trade_type") + "|"
                + paramMap.getString("order_amt") + "|"
                + paramMap.getString("mchnt_order_no") + "|"
                + paramMap.getString("txn_begin_ts") + "|"
                + paramMap.getString("goods_des") + "|"
                + paramMap.getString("term_id") + "|"
                + paramMap.getString("term_ip") + "|"
                + paramMap.getString("notify_url") + "|"
                + paramMap.getString("random_str") + "|"
                + paramMap.getString("version") + "|"
                + payPassword;
        return PayUtil.md5(str);
    }

}
