package com.java110.acct.payment.adapt.bbgpay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.acct.payment.adapt.bbgpay.lib.GmUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.JsonUtil;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.owner.OwnerAppUserDto;
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
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 北部湾银行支付厂家
 * <p>
 * // 以下数据先查询是否存在 存在则修改 不存在添加
 * INSERT INTO `TT`.`c_mapping` (`domain`, `name`, `key`, `value`, `remark`, `create_time`, `status_cd`)
 * VALUES ('WECHAT', 'native支付厂家', 'NATIVE_QRCODE_PAYMENT_ADAPT', 'bbgNativeQrcodePaymentFactoryAdapt', '', '2023-02-18 18:47:14', '0');

 */
@Service("bbgNativeQrcodePaymentFactoryAdapt")
public class BbgNativeQrcodePaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(BbgNativeQrcodePaymentFactoryAdapt.class);


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

    private static String VERSION = "1.0";

    private static String SIGN_TYPE = "RSA2";// 加密算法：SM4、RSA2

    private static String gzhPayUrl = "https://mbank.bankofbbg.com/www/corepaycer/getQrcodeLink";

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;


    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;
    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {

        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);
        String paymentPoolId = reqJson.getString("paymentPoolId");

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String tradeType = reqJson.getString("tradeType");
        String notifyUrl = UrlCache.getOwnerUrl() + "/app/payment/notify/nativeWechat/992020011134400001/" + paymentPoolId;


        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                payAmount,
                smallWeChatDto,
                paymentPoolId,
                notifyUrl
        );


        return resMap;
    }


    private Map<String, String> java110UnifieldOrder(String feeName, String orderNum,
                                                     double payAmount,
                                                     SmallWeChatDto smallWeChatDto,
                                                     String paymentPoolId,
                                                     String notifyUrl) throws Exception {

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String mchtNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "mchtNo_SM4");
        String productNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "productNo_SM4");
        String publicKey_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "publicKey_SM4");

        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("product_no", productNo_SM4);// 产品编号
        params.put("qr_type", "DYNAMIC");// 业务类型
        params.put("txn_type", "0");// 交易类型 0-实时;1-担保
        params.put("tran_no", orderNum);// 商户流水
        params.put("amt", payAmount);// 交易金额
        params.put("ware_name", feeName);// 商品名称
        params.put("ware_describe", "");// 商户数据包
        params.put("trx_trm_no", "");// 交易终端编号
        params.put("trans_source", "09");// 08：商户线下扫码09：商户线上收银10：一案一户缴款11：维修资金缴款12：房屋预售缴款
        params.put("asyn_url", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));// 通知地址

        String decryParams = EncryptDecryptFactory.execute(paymentPoolValueDtos, gzhPayUrl, params);
        JSONObject paramOut = JSONObject.parseObject(decryParams);
        if (!"SUCCESS".equals(paramOut.getString("status"))
                        || !"SUCCESS".equals(paramOut.getString("deal_status"))) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("return_message"));
        }

        if (!"0000".equals(paramOut.getString("return_code"))
                && !"0001".equals(paramOut.getString("return_code"))
        ) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("return_message"));
        }
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        resultMap.put("prepayId", paramOut.getString("tran_no"));
        resultMap.put("codeUrl", paramOut.getString("qr_link"));
        resultMap.put("code", "0");
        resultMap.put("msg", "下单成功");
        doSaveOnlinePay(smallWeChatDto, "无", orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());

        return resultMap;
    }


    @Override
    public PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(notifyPaymentOrderDto.getPaymentPoolId());
        paymentPoolValueDto.setCommunityId(notifyPaymentOrderDto.getCommunityId());
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);


        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }
        String privateKey_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "privateKey_SM4");

        String resXml = "";
        String param = notifyPaymentOrderDto.getParam();
        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        // 开始解密
        Map<String, Object> responseParams = JsonUtil.jsonToMap(param);
        if (!responseParams.containsKey("enc_data")) {
            System.err.println("通知失败");
            throw new IllegalArgumentException("通知失败");
        }
        String decryptStr = (String) responseParams.get("enc_data");
        String messageKey = (String) responseParams.get("message_key");
        String secretKey = GmUtil.decryptSm2(messageKey, privateKey_SM4);
        if (secretKey == null) {
            System.err.println("解密失败");
            throw new IllegalArgumentException("解密失败");
        }
        String decryParams = GmUtil.decryptSm4(decryptStr, secretKey);

        System.out.println("支付结果返回值(解密后):" + decryParams);

        JSONObject paramOut = JSONObject.parseObject(decryParams);
        String outTradeNo = paramOut.get("tran_no").toString();
        paymentOrderDto.setOrderId(outTradeNo);
        paymentOrderDto.setTransactionId(paramOut.get("txn_no").toString());

        doUpdateOnlinePay(outTradeNo, OnlinePayDto.STATE_COMPILE, "支付成功");

        JSONObject resJson = new JSONObject();
        resJson.put("return_code", "SUCCESS");
        resJson.put("return message", "成功");

        paymentOrderDto.setResponseEntity(new ResponseEntity<String>(resJson.toJSONString(), HttpStatus.OK));
        return paymentOrderDto;
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


    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String feeName,
                                 double money, String state, String message,
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

}
