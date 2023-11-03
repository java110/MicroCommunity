package com.java110.acct.payment.adapt.bbgpay;

import com.alibaba.fastjson.JSONObject;
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

import java.util.*;

/**
 * 微信支付
 */
@Service
public class QrCodeBbgPaymentAdapt implements IQrCodePaymentSMO {
    private static Logger logger = LoggerFactory.getLogger(QrCodeBbgPaymentAdapt.class);

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";

    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";


    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";

    private static String VERSION = "1.0";

    private static String SIGN_TYPE = "RSA2";// 加密算法：SM4、RSA2

    private static String gzhPayUrl = "https://mbank.bankofbbg.com/www/corepaycer/ScanCodePay";

    private static String queryUrl = "https://mbank.bankofbbg.com/www/corepaycer/QueryTxnInfo";// 交易查询地址

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

        String mchtNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "mchtNo_SM4");
        String productNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "productNo_SM4");
        String publicKey_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "publicKey_SM4");


        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("product_no", productNo_SM4);// 产品编号
        params.put("tran_no", orderNum);// 商户流水
        params.put("auth_code", authCode);// 码类型
        params.put("amt", payAmount);// 交易金额
        params.put("ware_name", feeName);// 商品名称
        params.put("device_ip", "172.0.0.1");// 商户数据包
        params.put("recog_no", "123123");// 交易终端编号
        doSaveOnlinePay(shopSmallWeChatDto, "-1", orderNum, feeName, payAmount, OnlinePayDto.STATE_WAIT, "待支付",paymentPoolValueDtos.get(0).getPpId());

        String decryParams = EncryptDecryptFactory.execute(paymentPoolValueDtos, gzhPayUrl, params);

        JSONObject paramOut = JSONObject.parseObject(decryParams);
        if (!"0000".equals(paramOut.getString("return_code"))
                || !"SUCCESS".equals(paramOut.getString("status"))
        ) {
            return new ResultVo(ResultVo.CODE_ERROR, "支付失败" + paramOut.getString("return_message"));

        }

        if ("FAIL".equals(paramOut.getString("deal_status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, "业务失败");
        }

        if ("SUCCESS".equals(paramOut.getString("deal_status"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");

            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, "等待用户支付中");
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
        String mchtNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "mchtNo_SM4");
        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("tran_no", orderNum);// 商户流水
        params.put("txn_no", "");// 支付流水

        // 对准备加签参数排序
        String decryParams = EncryptDecryptFactory.execute(paymentPoolValueDtos, queryUrl, params);

        /**
         * {"amt":"0.01","deal_status":"PROCESSING","jump_url":"","mcht_name":"广西蓉慧科技有限公司","mcht_no":"MCT2023060100029734",
         * "real_amt":"0.01","return_code":"5019","return_message":"用户正在输入密码，请等待","status":"SUCCESS",
         * "tran_no":"962023092519710062","txn_date":"20230925",
         * "txn_no":"P11082023092523543816778858","txn_time":"235438","ware_name":"云星花园-1栋1单元101室-住宅物业费"}
         */
        JSONObject paramOut = JSONObject.parseObject(decryParams);

        if ("PROCESSING".equals(paramOut.getString("deal_status")) && "5019".equals(paramOut.getString("return_code"))) {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        }

        if (!"SUCCESS".equals(paramOut.getString("status"))
                || !"SUCCESS".equals(paramOut.getString("deal_status"))) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("return_message"));
        }


        if (!"0000".equals(paramOut.getString("return_code"))
                && !"0001".equals(paramOut.getString("return_code"))
        ) {
            throw new IllegalArgumentException("支付失败" + paramOut.getString("return_message"));
        }

        if ("0000".equals(paramOut.getString("return_code"))) {
            doUpdateOnlinePay(orderNum, OnlinePayDto.STATE_COMPILE, "支付成功");
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        }
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

    private void doUpdateOnlinePay(String orderId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }
}
