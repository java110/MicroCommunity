package com.java110.acct.payment.adapt.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.ChinaUmsFactory;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.app.AppDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.intf.store.ISmallWechatV1InnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 支付宝支付
 *
 *
 * @desc add by 吴学文 15:33
 */
@Service("aliPaymentFactory")
public class AliPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(AliPaymentFactoryAdapt.class);


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


    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;


    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;


    @Autowired
    private ITempCarFeeCreateOrderV1InnerServiceSMO tempCarFeeCreateOrderV1InnerServiceSMOImpl;


    @Override
    public Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {

        SmallWeChatDto smallWeChatDto = getSmallWechat(reqJson);


        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String communityId = reqJson.getString("communityId");
        String notifyUrl = UrlCache.getOwnerUrl()+ "/app/payment/notify/common/992020011134400001/"+smallWeChatDto.getObjId();

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

        ResultVo resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                communityId,
                payAmount,
                openId,
                smallWeChatDto,
                notifyUrl
        );

        if (ResultVo.CODE_OK==resMap.getCode()) {
                resultMap.put("orderId", resMap.getData().toString());
            resultMap.put("code", "0");
            resultMap.put("msg", "下单成功");
            logger.info("【小程序支付】统一下单成功，返回参数:" + resultMap);
        } else {
            resultMap.put("code", resMap.getCode()+"");
            resultMap.put("msg", resMap.getMsg());
            logger.info("【小程序支付】统一下单失败，失败原因:" + resMap.getMsg());
        }
        return resultMap;
    }


    private ResultVo java110UnifieldOrder(String feeName, String orderNum,
                                            String communityId, double payAmount, String openId,
                                            SmallWeChatDto smallWeChatDto, String notifyUrl) throws Exception {

        //String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                CommunitySettingFactory.getValue(communityId, "APP_ID"),
                CommunitySettingFactory.getRemark(communityId, "APP_PRIVATE_KEY"),
                "json",
                "UTF-8",
                CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY"),
                "RSA2");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(MappingCache.getValue("ALIPAY", "temp"));
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNum);
        bizContent.put("total_amount", payAmount);
        bizContent.put("subject", feeName);
        bizContent.put("buyer_id", openId);
        bizContent.put("timeout_express", "10m");
        request.setBizContent(bizContent.toString());
        AlipayTradeCreateResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK,orderNum);
            } else {
                return new ResultVo(ResultVo.CODE_ERROR, response.getMsg());
            }
        } catch (AlipayApiException e) {
            throw new CmdException("支付宝下单失败" + e);
        }
    }


    @Override
    public PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {
        JSONObject reqJson = JSONObject.parseObject(notifyPaymentOrderDto.getParam());

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        Assert.jsonObjectHaveKey(reqJson, "out_trade_no", "请求报文中未包含订单信息");
        Assert.jsonObjectHaveKey(reqJson, "sign", "请求报文中未包含签名信息");

        String communityId = CommonCache.getAndRemoveValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE_COMMUNITY + reqJson.getString("out_trade_no"));

        String resultInfo = reqJson.getString("resultInfo");
        String[] temp = resultInfo.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        //把拆分数据放在map集合内
        for (int i = 0; i < temp.length; i++) {
            String[] arr = temp[i].split("=", 2); //通过"="号分割成2个数据
            String[] tempAagin = new String[arr.length]; //再开辟一个数组用来接收分割后的数据
            for (int j = 0; j < arr.length; j++) {
                tempAagin[j] = arr[j];
            }
            map.put(tempAagin[0], tempAagin[1]);
        }
        System.out.println(map);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map,
                    CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY")
                    , "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if (!signVerified) {
            throw new CmdException("签名失败");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", reqJson.getString("out_trade_no"));
        ResponseEntity responseEntity = tempCarFeeCreateOrderV1InnerServiceSMOImpl.notifyOrder(paramIn);
        paymentOrderDto.setResponseEntity(responseEntity);
        return paymentOrderDto;
    }

    public int confirmPayFee(JSONObject map, PaymentOrderDto paymentOrderDto) {
        String appId;
        //兼容 港币交易时 或者微信有时不会掉参数的问题
        if (map.containsKey("wId")) {
            String wId = map.get("wId").toString();
            wId = wId.replace(" ", "+");
            appId = WechatFactory.getAppId(wId);
        } else {
            appId = map.get("appid").toString();
        }
        JSONObject paramIn = new JSONObject();
        paramIn.put("appId", appId);
        SmallWeChatDto smallWeChatDto = getSmallWechat(paramIn);
        //String sign = PayUtil.createChinaUmsSign(paramMap, smallWeChatDto.getPayPassword());
        String preSign = map.getString("preSign");
        String text = preSign + smallWeChatDto.getPayPassword();
        System.out.println("待签名字符串：" + text);
        String sign = DigestUtils.sha256Hex(getContentBytes(text)).toUpperCase();

        if (!sign.equals(map.get("sign"))) {
            throw new IllegalArgumentException("鉴权失败");
        }
        //JSONObject billPayment = JSONObject.parseObject(map.getString("billPayment"));
        String outTradeNo = map.get("merOrderId").toString();
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


    // 根据编码类型获得签名内容byte[]
    public static byte[] getContentBytes(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误");
        }
    }

}
