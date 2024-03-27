package com.java110.acct.payment.adapt.plutus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.PlutusFactory;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付
 * WECHAT
 * PAY_QR_ADAPT
 */
@Service
public class QrCodePlutusPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodePlutusPaymentAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    public static final String PAY_UNIFIED_ORDER_URL = "https://api.plutuspay.com/open/v2/pay";

    public static final String QUERY_ORDER_URL = "https://api.plutuspay.com/open/v2/query";




    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName,String paymentPoolId) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        SmallWeChatDto shopSmallWeChatDto = null;

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            shopSmallWeChatDto = new SmallWeChatDto();
            shopSmallWeChatDto.setObjId(communityId);
            shopSmallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            shopSmallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
        } else {
            shopSmallWeChatDto = smallWeChatDtos.get(0);
        }

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_KEY");
        String privateKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PRIVATE_KEY");
        String devId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_DEV_ID");
        String publicKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PUBLIC_KEY");

        JSONObject paramMap = new JSONObject();
        paramMap.put("sn", mchId); // 富友分配给二级商户的商户号
        paramMap.put("outTradeId", orderNum);
        paramMap.put("authCode", authCode);
        paramMap.put("tradeAmount", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("payTypeId", "0");

        String param = PlutusFactory.Encryption(paramMap.toJSONString(), privateKey,key,devId);
        System.out.println(param);
        doSaveOnlinePay(shopSmallWeChatDto, "-1", orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT,
                "待支付",paymentPoolValueDtos.get(0).getPpId());

        String str = PlutusFactory.post(PAY_UNIFIED_ORDER_URL, param);
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");

        //验签
        Boolean verify = PlutusFactory.verify256(content, Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), key);
        //服务器返回内容
        String paramOut = new String(bb);

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if ("1".equals(paramObj.getString("status"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");

            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, json.getString("msg")+paramObj.getString("remark"));
        }

    }

    public ResultVo checkPayFinish(String communityId, String orderNum,String paymentPoolId) {
        SmallWeChatDto shopSmallWeChatDto = null;
        Map<String, String> result = null;

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDtos == null && smallWeChatDtos.size() < 1) {
            shopSmallWeChatDto = new SmallWeChatDto();
            shopSmallWeChatDto.setObjId(communityId);
            shopSmallWeChatDto.setAppId(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appId"));
            shopSmallWeChatDto.setAppSecret(MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, "appSecret"));
        } else {
            shopSmallWeChatDto = smallWeChatDtos.get(0);
        }
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        paymentPoolValueDto.setCommunityId(communityId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);


        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_KEY");
        String privateKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PRIVATE_KEY");
        String devId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_DEV_ID");
        String publicKey = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "PLUTUS_PUBLIC_KEY");


        JSONObject paramMap = new JSONObject();
        paramMap.put("sn", mchId); // 富友分配给二级商户的商户号
        paramMap.put("outTradeId", orderNum);
        System.out.println(paramMap.toJSONString());

        String param = PlutusFactory.Encryption(paramMap.toJSONString(), privateKey, key,devId);
        System.out.println(param);

        String str = PlutusFactory.post(QUERY_ORDER_URL, param);
        System.out.println(str);

        JSONObject json = JSON.parseObject(str);

        String signature = json.getString("signature");
        String content = json.getString("content");

        //验签
        Boolean verify = PlutusFactory.verify256(content, Base64.decode(signature), publicKey);
        //验签成功
        if (!verify) {
            throw new IllegalArgumentException("支付失败签名失败");
        }
        //解密
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), key);
        //服务器返回内容
        String paramOut = new String(bb);

        JSONObject paramObj = JSONObject.parseObject(paramOut);

        if ("1".equals(paramObj.getString("status"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else if ("0".equals(paramObj.getString("status"))) {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, json.getString("msg")+paramObj.getString("remark"));
        }
    }

    private void doSaveOnlinePay(SmallWeChatDto smallWeChatDto, String openId, String orderId, String feeName, double money,
                                 String state, String message,
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
