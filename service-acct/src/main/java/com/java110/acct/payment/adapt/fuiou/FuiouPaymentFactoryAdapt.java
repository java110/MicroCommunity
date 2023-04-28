package com.java110.acct.payment.adapt.fuiou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.PlutusFactory;
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
import org.bouncycastle.util.encoders.Base64;
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
 * 富友 支付
 * 此实现方式为  通过 富友支付 去下单不直接去掉微信
 * <p>
 * 商户调用此接口则用户可使用支付宝或微信进行支付。
 * 本接口支持： 微信公众号、 微信小程序、 微信 APP， 支付宝服务窗等
 * <p>
 * <p>
 * 说明：信息通过 http 或 https 形式 post 请求递交给前置系统，编码必须为 UTF-8
 * Json 格式参数名：如下表
 * 参数值：如下表
 * 测试地址： https://aipaytest.fuioupay.com/aggregatePay/wxPreCreate
 * 生产地址： https://aipay.fuioupay.com/aggregatePay/wxPreCreate
 * 生产地址 2： https://aipay-xs.fuioupay.com/aggregatePay/wxPreCreate
 * <p>
 * 该接口常应用于聚合二维码（静态二维码、统一收款码、台卡等不同叫法），用户扫二维码进入微信公众号/支付宝服务窗
 * /QQJS 页面，页面调此接口生成订单，接受订单参数后调起官方支付接口支付。详见公众号/服务窗对接流程
 * 步骤 1：用户通过支付宝(服务窗)、微信(公众号)进入到商户 H5 页面，或者是通过扫描台卡进入。
 * 步骤 2：用户选择商品、输入支付金额等进行下单支付
 * 步骤 3：商户将订单信息发送给富友，返回支付信息(用于调起支付宝、微信的参数)。
 * 步骤 4：商户拿到支付信息后调起微信或者支付宝进行支付
 * 步骤 5：支付结果以回调(2.5)的方式通知到商户
 *
 * @desc add by 吴学文 15:33
 */
@Service("fuiouPaymentFactory")
public class FuiouPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(FuiouPaymentFactoryAdapt.class);


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

    public static final String PAY_UNIFIED_ORDER_URL = "https://aipay.fuioupay.com/aggregatePay/wxPreCreate";


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
