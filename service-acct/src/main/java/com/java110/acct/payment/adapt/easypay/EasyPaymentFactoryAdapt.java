package com.java110.acct.payment.adapt.easypay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentFactoryAdapt;
import com.java110.acct.payment.adapt.bbgpay.EncryptDecryptFactory;
import com.java110.acct.payment.adapt.bbgpay.lib.GmUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.JsonUtil;
import com.java110.acct.payment.adapt.easypay.utils.HttpConnectUtils;
import com.java110.core.context.ICmdDataFlowContext;
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

import java.io.IOException;
import java.util.*;

/**
 * 北部湾银行支付厂家
 * <p>
 * https://doc.eycard.cn/web/#/285?page_id=4149
 */
@Service("easyPaymentFactoryAdapt")
public class EasyPaymentFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(EasyPaymentFactoryAdapt.class);


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

            if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
                throw new IllegalArgumentException("未找到开放账号信息");
            }
            openId = ownerAppUserDtos.get(0).getOpenId();
        }


        logger.debug("【小程序支付】 统一下单开始, 订单编号=" + paymentOrderDto.getOrderId());
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), paymentOrderDto.getMoney());
        //添加或更新支付记录(参数跟进自己业务需求添加)

        JSONObject resMap = null;
        resMap = this.java110UnifieldOrder(paymentOrderDto.getName(),
                paymentOrderDto.getOrderId(),
                tradeType,
                payAmount,
                openId,
                smallWeChatDto,
                paymentPoolId,
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
                                            SmallWeChatDto smallWeChatDto,
                                            String paymentPoolId,
                                            String notifyUrl) throws Exception {
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String ORGID = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGID"); // 客户编号
        String ORGMERCODE = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGMERCODE");
        String ORGTERMNO = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGTERMNO");
        String EASYPAY_PUBLIC_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "EASYPAY_PUBLIC_KEY");
        String MER_RSA_PRIVATE_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "MER_RSA_PRIVATE_KEY");


        if (feeName.length() > 127) {
            feeName = feeName.substring(0, 126);
        }

        JsApi.DataBean dataBean = new JsApi.DataBean();
        dataBean.setOrgFrontUrl(UrlCache.getOwnerUrl());
        dataBean.setOrgBackUrl(notifyUrl + "?wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()));

        // tradeCode 只有  WAJS1:支付宝-生活号支付;WAJS2:支付宝-小程序支付;WTJS1:微信-公众号支付;
        // WTJS2:微信-小程序支付;WUJS1:银联二维码-JS支付(payerId 传入 仿真app的 用户名，比如admin4)   这几种选择

        dataBean.setTradeCode("WTJS2");
        // 金额,单位分；取值范围：1-10000000000

        dataBean.setPayerId(openid);  // 凯杰 微信
        dataBean.setWxSubAppid(smallWeChatDto.getAppid());         // 凯杰 微信

        //订单信息（订单明细内容，如订单标题、订单描述等）
        dataBean.setOrderInfo(feeName);
        dataBean.setTradeAmt(Integer.parseInt(PayUtil.moneyToIntegerStr(payAmount)));
        //订单支付超时时间；长度：8。单位：秒, 不填默认为300（5分钟)
        //dataBean.setTimeoutMinutes(300);

        JsApi request = new JsApi();
        request.setOrgId(ORGID);
        request.setOrgMercode(ORGMERCODE);
        request.setOrgTermno(ORGTERMNO);
        request.setSignType(BasePay.SIGN_TYPE_RSA256);
        // 交易流水，28位定长，规则：orgId 前四位+后四位+yyyyMMddhhmmss+6位自定义 合计28位长度（唯一）
        request.setOrgTrace(orderNum);

        //request.setAppendData(appendData);

        request.setData(dataBean);
        // 使用商户私钥对data字段加签
        String sign = BasePay.sign(dataBean, MER_RSA_PRIVATE_KEY);
        request.setSign(sign);
        doSaveOnlinePay(smallWeChatDto, openid, orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());

        String requestStr = JSONObject.toJSONString(request);

        System.out.println("反反复复：" + requestStr);
        String response = "";

        response = HttpConnectUtils.sendHttpSRequest(BasePay.BASE_URL + "/standard/jsapi", requestStr, "JSON", null);
        System.out.println("\n响应报文：" + response);
        BasePay.checkSign(response, EASYPAY_PUBLIC_KEY);

        JSONObject paramOut = JSONObject.parseObject(response);
        if (!"000000".equals(paramOut.getString("sysRetcode"))) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("sysRetmsg"));
        }

        JSONObject data = paramOut.getJSONObject("data");

        String finRetcode = paramOut.getJSONObject("data").getString("finRetcode");

        if (!"00".equals(finRetcode) && !"99".equals(finRetcode)) {
            throw new IllegalArgumentException("支付失败,相应finRetcode为：" + finRetcode);
        }

        //wxWcPayData
        String wxWcPayData = data.getString("wxWcPayData");

        return JSONObject.parseObject(wxWcPayData);
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
        String EASYPAY_PUBLIC_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "EASYPAY_PUBLIC_KEY");

        String param = notifyPaymentOrderDto.getParam();
        System.out.println("支付结果返回值:" + param);

        if(!BasePay.checkSign(param, EASYPAY_PUBLIC_KEY)){
            throw new IllegalArgumentException("签名错误");
        }

        JSONObject paramJson = JSONObject.parseObject(param);

//        String sign = BasePay.checkSign(paramJson, EASYPAY_PUBLIC_KEY);
//
//        if (!sign.equals(paramJson.getString("sign"))) {
//            throw new IllegalArgumentException("签名错误");
//        }

        JSONObject paramOut = paramJson.getJSONObject("data");


        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();

        String outTradeNo = paramOut.getString("oriOrgTrace");
        paymentOrderDto.setOrderId(outTradeNo);
        paymentOrderDto.setTransactionId(paramOut.getString("outTrace"));

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
                                 double money, String state, String message,String ppId) {
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
