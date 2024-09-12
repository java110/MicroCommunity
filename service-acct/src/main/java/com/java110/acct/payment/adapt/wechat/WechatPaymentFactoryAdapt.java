package com.java110.acct.payment.adapt.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IComputeDeductionIntegral;
import com.java110.acct.integral.IComputeGiftIntegral;
import com.java110.acct.integral.IDeductionIntegral;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.integral.DeductionIntegralDto;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 微信支付厂家类
 * <p>
 * 微信官方原生 支付实现类
 */
@Service("wechatPaymentFactory")
public class WechatPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(WechatPaymentFactoryAdapt.class);


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


    public static final String wxPayUnifiedOrder = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;


    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatIntegralShareAcct wechatIntegralShareAcct;

    @Autowired
    private IComputeGiftIntegral computeGiftIntegralImpl;

    @Autowired
    private IComputeDeductionIntegral computeDeductionIntegralImpl;

    @Autowired
    private IDeductionIntegral deductionIntegralImpl;


    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {

        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);

        String paymentPoolId = reqJson.getString("paymentPoolId");
        String useIntegral = reqJson.getString("useIntegral");
        paymentOrderDto.setUseIntegral(useIntegral);


        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String tradeType = reqJson.getString("tradeType");
        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/wechat/992020011134400001/" + paymentPoolId;

        String openId = reqJson.getString("openId");

        if (StringUtil.isEmpty(openId)) {
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

            if (ListUtil.isNull(ownerAppUserDtos)) {
                throw new IllegalArgumentException("未找到开放账号信息");
            }
            openId = ownerAppUserDtos.get(0).getOpenId();
        }


        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (ListUtil.isNull(paymentPoolValueDtos)) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }


        Map<String, String> resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                tradeType,
                payAmount,
                openId,
                smallWeChatDto,
                paymentPoolValueDtos,
                notifyUrl,
                paymentOrderDto
        );

        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");

        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            if (TRADE_TYPE_JSAPI.equals(tradeType)) {

                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("package", "prepay_id=" + resMap.get("prepay_id"));
                resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, key));
            } else if (TRADE_TYPE_APP.equals(tradeType)) {
                resultMap.put("appId", smallWeChatDto.getAppId());
                resultMap.put("timeStamp", PayUtil.getCurrentTimeStamp());
                resultMap.put("nonceStr", PayUtil.makeUUID(32));
                resultMap.put("partnerid", smallWeChatDto.getMchId());
                resultMap.put("prepayid", resMap.get("prepay_id"));
                //resultMap.put("signType", "MD5");
                resultMap.put("sign", PayUtil.createSign(resultMap, key));
            } else if (TRADE_TYPE_NATIVE.equals(tradeType)) {
                resultMap.put("prepayId", resMap.get("prepay_id"));
                resultMap.put("codeUrl", resMap.get("code_url"));
            }
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


    private Map<String, String> java110UnifieldOrder(String feeName,
                                                     String tradeType, double payAmount, String openid,
                                                     SmallWeChatDto smallWeChatDto,
                                                     List<PaymentPoolValueDto> paymentPoolValueDtos,
                                                     String notifyUrl,
                                                     PaymentOrderDto paymentOrderDto
    ) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);
        if(!StringUtil.isNumber(paymentOrderDto.getCycles())){
            paymentOrderDto.setCycles("1");
        }

        //todo 计算赠送积分
        GiftIntegralDto giftIntegralDto = computeGiftIntegralImpl.gift(payAmount, Integer.parseInt(paymentOrderDto.getCycles()),
                paymentPoolValueDtos.get(0).getCommunityId());

        if (giftIntegralDto != null && giftIntegralDto.getMoney() > 0) {
            paymentOrderDto.setIsShare("Y");
            giftIntegralDto.setUserId(paymentOrderDto.getUserId());
            paymentOrderDto.setGiftIntegralDto(giftIntegralDto);
        }

        //todo 计算 积分抵扣
        if ("Y".equals(paymentOrderDto.getUseIntegral())) {
            DeductionIntegralDto deductionIntegralDto = computeDeductionIntegralImpl.deduction(paymentOrderDto.getUserId(),
                    paymentOrderDto.getOrderId(),
                    paymentPoolValueDtos.get(0).getCommunityId());
            payAmount = payAmount - deductionIntegralDto.getMoney();
            if (payAmount <= 0) {
                throw new IllegalArgumentException("积分金额大于支付金额");
            }
        }

        //这里防止 小数点不是 2位 比如 3位之类的 微信平台不支持
        payAmount = MoneyUtil.computePriceScale(payAmount, "1", 2);


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
        paramMap.put("out_trade_no", paymentOrderDto.getOrderId());
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("notify_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));
        paramMap.put("trade_type", tradeType);
        paramMap.put("openid", openid);
        //加入分账申请
        if (PaymentOrderDto.SHARE_ACCT.equals(paymentOrderDto.getIsShare())) {
            paramMap.put("profit_sharing", "Y");
            paymentOrderDto.getGiftIntegralDto().setAppId(smallWeChatDto.getAppId());
            paymentOrderDto.getGiftIntegralDto().setMchId(mchId);
            paymentOrderDto.getGiftIntegralDto().setMchKey(key);
            paymentOrderDto.getGiftIntegralDto().setUserId(paymentOrderDto.getUserId());
            paymentOrderDto.getGiftIntegralDto().setCertPath(getWechatCertPath(paymentPoolValueDtos.get(0).getPpId()));
            paymentOrderDto.getGiftIntegralDto().setOrderId(paymentOrderDto.getOrderId());
            CommonCache.setValue("integral_share_acct_" + paymentOrderDto.getOrderId(), JSONObject.toJSONString(paymentOrderDto.getGiftIntegralDto()), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        }

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            paramMap.put("sub_appid", smallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", mchId);//起调小程序的商户号
            paramMap.put("sub_openid", openid);
            paramMap.remove("openid");
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
        doSaveOnlinePay(smallWeChatDto, openid, paymentOrderDto.getOrderId(), feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付", paymentPoolValueDtos.get(0).getPpId());
        return PayUtil.xmlStrToMap(responseEntity.getBody());
    }

    private String getWechatCertPath(String ppId) {

        //todo

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(ppId);
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);

        if (ListUtil.isNull(paymentPoolDtos)) {
            throw new IllegalArgumentException("未配置支付");
        }

        if (StringUtil.isEmpty(paymentPoolDtos.get(0).getCertPath())) {
            throw new IllegalArgumentException("未配置证书");
        }

        return paymentPoolDtos.get(0).getCertPath();
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

    public int confirmPayFee(Map<String, Object> map, PaymentOrderDto paymentOrderDto, NotifyPaymentOrderDto notifyPaymentOrderDto) throws Exception {
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

        if (ListUtil.isNull(paymentPoolValueDtos)) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        // String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");

        String sign = PayUtil.createSign(paramMap, key);

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }

        String outTradeNo = map.get("out_trade_no").toString();
        paymentOrderDto.setOrderId(outTradeNo);
        paymentOrderDto.setTransactionId(map.get("transaction_id").toString());

        doUpdateOnlinePay(outTradeNo, OnlinePayDto.STATE_COMPILE, "支付成功");

        //todo 将抵扣积分转给物业
        deductionIntegralImpl.deduction(paymentOrderDto.getOrderId());


        String giftIntegralDtoStr = CommonCache.getAndRemoveValue("integral_share_acct_" + paymentOrderDto.getOrderId());

        if (StringUtil.isEmpty(giftIntegralDtoStr)) {
            return 1;
        }

        GiftIntegralDto giftIntegralDto = JSONObject.parseObject(giftIntegralDtoStr, GiftIntegralDto.class);
        if (giftIntegralDto == null) {
            return 1;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*60*1000);
                    //todo 添加分账商家
                    JSONArray params = wechatIntegralShareAcct.addReceivers(giftIntegralDto);


                    //todo 申请分账
                    JSONObject applyResult = wechatIntegralShareAcct.applyProfileShare(paymentOrderDto, giftIntegralDto, params);

                    if (applyResult.containsKey("result_code") && "SUCCESS".equals(applyResult.getString("result_code"))) {
                        JSONObject finishParam = new JSONObject();
                        finishParam.put("out_order_no", applyResult.getString("order_id"));
                        //完结分账 单次分账
                        //finishProfileShare(storeOrderDto, smallWeChatDto, finishParam);
                    } else {
                        throw new IllegalArgumentException("分账失败:" + applyResult.toJSONString());
                    }

                    // 删除接收方
                    wechatIntegralShareAcct.deleteReceivers(giftIntegralDto);

                    // todo 给用户赠送积分
                    wechatIntegralShareAcct.sendIntegral(giftIntegralDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return 1;
    }


    private SmallWeChatDto getSmallWechat(JSONObject paramIn) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(paramIn.getString("communityId"));
        smallWeChatDto.setAppId(paramIn.getString("appId"));
        smallWeChatDto.setPage(1);
        smallWeChatDto.setRow(1);
        List<SmallWeChatDto> smallWeChatDtos = smallWechatV1InnerServiceSMOImpl.querySmallWechats(smallWeChatDto);

        if (ListUtil.isNull(smallWeChatDtos)) {
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


    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String feeName,
                                 double money, String state, String message, String ppId) {
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

}
