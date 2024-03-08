package com.java110.acct.payment.adapt.easypay;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.adapt.easypay.utils.HttpConnectUtils;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
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
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 微信支付
 */
@Service
public class QrCodeEasyPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodeEasyPaymentAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";


    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName, String paymentPoolId) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;
        logger.debug("resMap=" + resMap);
        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);
        SmallWeChatDto shopSmallWeChatDto = null;
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
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);

        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }

        String ORGID = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGID"); // 客户编号
        String ORGMERCODE = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGMERCODE");
        String ORGTERMNO = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGTERMNO");
        String EASYPAY_PUBLIC_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "EASYPAY_PUBLIC_KEY");
        String MER_RSA_PRIVATE_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "MER_RSA_PRIVATE_KEY");

        JSONObject paramIn = new JSONObject();
        paramIn.put("orgId", ORGID);
        paramIn.put("orgMercode", ORGMERCODE);
        paramIn.put("orgTermno", ORGTERMNO);
        paramIn.put("signType", BasePay.SIGN_TYPE_RSA256);
        paramIn.put("orgTrace", orderNum);


        JSONObject data = new JSONObject();
        data.put("authCode", authCode);
        data.put("tradeAmt", Integer.parseInt(PayUtil.moneyToIntegerStr(payAmount)));
        data.put("orderInfo", feeName);
        paramIn.put("data", data);

        // 使用商户私钥对data字段加签
        String sign = BasePay.sign(data, MER_RSA_PRIVATE_KEY);
        paramIn.put("sign", sign);

        String requestStr = paramIn.toJSONString();
        doSaveOnlinePay(shopSmallWeChatDto, "-1", orderNum, feeName, payAmount,
                OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());


        String response = HttpConnectUtils.sendHttpSRequest(BasePay.BASE_URL + "/standard/scanPay", requestStr, "JSON", null);
        System.out.println("\n响应报文：" + response);
        BasePay.checkSign(response, EASYPAY_PUBLIC_KEY);

        JSONObject paramOut = JSONObject.parseObject(response);
        if (!"000000".equals(paramOut.getString("sysRetcode"))) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("sysRetmsg"));
        }

        JSONObject resData = paramOut.getJSONObject("data");

        if ("00".equals(resData.getString("finRetcode"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待用户支付中");
        }
    }

    public ResultVo checkPayFinish(String communityId, String orderNum, String paymentPoolId) {
        Map<String, String> result = null;
        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setPpId(paymentPoolId);
        paymentPoolValueDto.setCommunityId(communityId);
        List<PaymentPoolValueDto> paymentPoolValueDtos = paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);


        if (paymentPoolValueDtos == null || paymentPoolValueDtos.isEmpty()) {
            throw new IllegalArgumentException("配置错误,未配置参数");
        }
        String ORGID = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGID"); // 客户编号
        String ORGMERCODE = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGMERCODE");
        String ORGTERMNO = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "ORGTERMNO");
        String EASYPAY_PUBLIC_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "EASYPAY_PUBLIC_KEY");
        String MER_RSA_PRIVATE_KEY = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "MER_RSA_PRIVATE_KEY");

        JSONObject paramIn = new JSONObject();
        paramIn.put("orgId", ORGID);
        paramIn.put("orgMercode", ORGMERCODE);
        paramIn.put("orgTermno", ORGTERMNO);
        paramIn.put("signType", BasePay.SIGN_TYPE_RSA256);
        paramIn.put("orgTrace", GenerateCodeFactory.getGeneratorId("10"));
        JSONObject data = new JSONObject();
        data.put("oriOrgTrace", orderNum);
        paramIn.put("data", data);
        String sign = BasePay.sign(data, MER_RSA_PRIVATE_KEY);
        paramIn.put("sign", sign);

        String requestStr = paramIn.toJSONString();

        String response = null;
        try {
            response = HttpConnectUtils.sendHttpSRequest(BasePay.BASE_URL + "/standard/tradeQuery", requestStr, "JSON", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n响应报文：" + response);
        BasePay.checkSign(response, EASYPAY_PUBLIC_KEY);

        JSONObject paramOut = JSONObject.parseObject(response);
        if (!"000000".equals(paramOut.getString("sysRetcode"))) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("sysRetmsg"));
        }

        JSONObject resData = paramOut.getJSONObject("data");

        if ("00".equals(resData.getString("finRetcode"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");

            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, "等待用户支付中");
        }

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

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }
}
