package com.java110.acct.payment.adapt.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.paymentPoolValue.PaymentPoolValueDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.PayUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付
 */
@Service
public class QrCodeWechatPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodeWechatPaymentAdapt.class);

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
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

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
        if (smallWeChatDtos == null || smallWeChatDtos.size() < 1) {
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

        String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
        String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");

        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", shopSmallWeChatDto.getAppId());
        paramMap.put("mch_id", mchId);
        paramMap.put("nonce_str", PayUtil.makeUUID(32));
        paramMap.put("body", feeName);
        paramMap.put("out_trade_no", orderNum);
        paramMap.put("total_fee", PayUtil.moneyToIntegerStr(payAmount));
        paramMap.put("spbill_create_ip", PayUtil.getLocalIp());
        paramMap.put("auth_code", authCode);

        String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
        if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
            paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
            paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户号
            paramMap.put("sub_appid", shopSmallWeChatDto.getAppId());//起调小程序appid
            paramMap.put("sub_mch_id", mchId);//起调小程序的商户号
            paramMap.remove("openid");
        }
        paramMap.put("sign", PayUtil.createSign(paramMap, key));
//转换为xml
        String xmlData = PayUtil.mapToXml(paramMap);

        logger.debug("调用支付统一下单接口" + xmlData);

        doSaveOnlinePay(shopSmallWeChatDto, "-1", orderNum, feeName, payAmount,
                OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());


        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                WechatConstant.wxMicropayUnifiedOrder, xmlData, String.class);

        logger.debug("统一下单返回" + responseEntity);
//请求微信后台，获取预支付ID
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
        }
        resMap = PayUtil.xmlStrToMap(responseEntity.getBody());

        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");

            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            String msg = resMap.get("return_msg");
            if (!StringUtil.isEmpty(resMap.get("err_code_des"))) {
                msg = resMap.get("err_code_des");
            }
            return new ResultVo(ResultVo.CODE_ERROR, msg);
        }
    }

    public ResultVo checkPayFinish(String communityId, String orderNum,String paymentPoolId) {
        SmallWeChatDto shopSmallWeChatDto = null;
        Map<String, String> result = null;
        try {
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

            String mchId = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_MCHID");
            String key = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "WECHAT_KEY");

            SortedMap<String, String> paramMap = new TreeMap<String, String>();
            paramMap.put("appid", shopSmallWeChatDto.getAppId());
            paramMap.put("mch_id", mchId);
            paramMap.put("nonce_str", PayUtil.makeUUID(32));
            paramMap.put("out_trade_no", orderNum);
            String paySwitch = MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_PAY_SWITCH);
            if (WECHAT_SERVICE_PAY_SWITCH_ON.equals(paySwitch)) {
                paramMap.put("appid", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_APP_ID));  //服务商appid，是服务商注册时公众号的id
                paramMap.put("mch_id", MappingCache.getValue(DOMAIN_WECHAT_PAY, WECHAT_SERVICE_MCH_ID));  //服务商商户
                paramMap.put("sub_mch_id", mchId);
            }
            paramMap.put("sign", PayUtil.createSign(paramMap, key));
//转换为xml
            String xmlData = PayUtil.mapToXml(paramMap);

            logger.debug("调用支付统一下单接口" + xmlData);

            ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(
                    WechatConstant.wxOrderQuery, xmlData, String.class);

            logger.debug("统一下单返回" + responseEntity);
//请求微信后台，获取预支付ID
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("支付失败" + responseEntity.getBody());
            }
            result = PayUtil.xmlStrToMap(responseEntity.getBody());

        } catch (Exception e) {
            logger.error("请求失败", e);
            return new ResultVo(ResultVo.CODE_WAIT_PAY, e.getLocalizedMessage());
        }
        if (!"SUCCESS".equals(result.get("trade_state"))) {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        }

        doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");

        return new ResultVo(ResultVo.CODE_OK, "支付成功");
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
