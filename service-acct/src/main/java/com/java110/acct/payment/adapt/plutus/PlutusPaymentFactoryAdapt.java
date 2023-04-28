package com.java110.acct.payment.adapt.plutus;

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
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * http://open.plutuspay.com/index.html
 * 支付厂家类
 * <p>
 */
@Service("plutusPaymentFactory")
public class PlutusPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(PlutusPaymentFactoryAdapt.class);


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


    public static final String PAY_UNIFIED_ORDER_URL = "https://api.plutuspay.com/open/v2/jsPay";
    public static final String PAY_UNIFIED_ORDER_NATIVE_URL = "https://api.plutuspay.com/open/v2/preCreate";


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
        String notifyUrl = UrlCache.getOwnerUrl()+"/app/payment/notify/wechat/992020011134400001/"+smallWeChatDto.getObjId();

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


        if (TRADE_TYPE_JSAPI.equals(tradeType)) {
            resultMap.putAll(JSONObject.toJavaObject(resMap, Map.class));
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
        return resultMap;
    }


    private JSONObject java110UnifieldOrder(String feeName, String orderNum,
                                                     String tradeType, double payAmount, String openid,
                                                     SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        JSONObject paramMap = new JSONObject();

        paramMap.put("appId", smallWeChatDto.getAppId());
        paramMap.put("openId", openid);
        paramMap.put("sn", smallWeChatDto.getMchId()); // 富友分配给二级商户的商户号
        paramMap.put("outTradeId", orderNum);
        if (OwnerAppUserDto.APP_TYPE_WECHAT_MINA.equals(tradeType)) {
            paramMap.put("isMiniProgram", true);
        }
        paramMap.put("tradeAmount", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("payTypeId", "1003");
        paramMap.put("notifyUrl", notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));

        logger.debug("调用支付统一下单接口" + paramMap.toJSONString());
        String privateKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "PLUTUS_PRIVATE_KEY");
        String devId = CommunitySettingFactory.getValue(smallWeChatDto.getObjId(), "PLUTUS_DEV_ID");

        String param = PlutusFactory.Encryption(paramMap.toJSONString(), privateKey, smallWeChatDto.getPayPassword(), devId);
        System.out.println(param);
        String str = "";
        if (TRADE_TYPE_NATIVE.equals(tradeType)) {
            str = PlutusFactory.post(PAY_UNIFIED_ORDER_NATIVE_URL, param);
        } else {
            str = PlutusFactory.post(PAY_UNIFIED_ORDER_URL, param);
        }
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");
        String publicKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(), "PLUTUS_PUBLIC_KEY");

        //验签
        Boolean verify = PlutusFactory.verify256(content, Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), smallWeChatDto.getPayPassword());
        //服务器返回内容
        String paramOut = new String(bb);

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        logger.debug("统一下单返回" + paramOut);

        if (!"00".equals(paramObj.getString("status"))) {
            throw new IllegalArgumentException("支付失败" + paramObj.getString("error"));
        }

        if (TRADE_TYPE_NATIVE.equals(tradeType)) {
            paramObj.put("code", 0);
            return paramObj;
        } else {
            return paramObj.getJSONObject("payInfo");
        }
    }


    @Override
    public PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String param = notifyPaymentOrderDto.getParam();

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        JSONObject json = JSON.parseObject(param);

        String signature = json.getString("signature");
        String content = json.getString("content");
        String appId = null;
        if (json.containsKey("wId")) {
            String wId = json.get("wId").toString();
            wId = wId.replace(" ", "+");
            appId = WechatFactory.getAppId(wId);
        } else {
            appId = json.get("appid").toString();
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("appId", appId);
        paramIn.put("communityId",notifyPaymentOrderDto.getCommunityId());
        SmallWeChatDto smallWeChatDto = getSmallWechat(paramIn);
        if (smallWeChatDto == null) {
            throw new IllegalArgumentException("未配置公众号或者小程序信息");
        }
        String publicKey = CommunitySettingFactory.getRemark(smallWeChatDto.getObjId(),"PLUTUS_PUBLIC_KEY");
        //验签
        Boolean verify = PlutusFactory.verify256(content, org.bouncycastle.util.encoders.Base64.decode(signature),publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), smallWeChatDto.getPayPassword());
        //服务器返回内容
        String paramOut = new String(bb);
            JSONObject map = JSONObject.parseObject(paramOut);
            logger.info("【银联支付回调】 回调数据： \n" + map);
            //更新数据
            paymentOrderDto.setOrderId(map.getString("outTransId"));
        paymentOrderDto.setResponseEntity(new ResponseEntity<String>("SUCCESS", HttpStatus.OK));
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

}
