package com.java110.acct.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.adapt.bbgpay.EncryptDecryptFactory;
import com.java110.acct.payment.adapt.bbgpay.lib.AesEncrypt;
import com.java110.acct.payment.adapt.bbgpay.lib.CAUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.HttpRequestUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.JsonUtil;
import com.java110.acct.smo.IQrCodePaymentSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.PayUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Override
    public ResultVo pay(String communityId, String orderNum, double money, String authCode, String feeName) throws Exception {
        logger.info("【小程序支付】 统一下单开始, 订单编号=" + orderNum);
        SortedMap<String, String> resultMap = new TreeMap<String, String>();
        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
        double payAmount = PayUtil.getPayAmountByEnv(MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV"), money);
        //添加或更新支付记录(参数跟进自己业务需求添加)

        Map<String, String> resMap = null;
        logger.debug("resMap=" + resMap);
        String systemName = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAY_GOOD_NAME);

        String mchtNo_SM4 = CommunitySettingFactory.getValue(communityId, "mchtNo_SM4");
        String productNo_SM4 = CommunitySettingFactory.getValue(communityId, "productNo_SM4");
        String publicKey_SM4 = CommunitySettingFactory.getValue(communityId, "publicKey_SM4");

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

        String decryParams = EncryptDecryptFactory.execute(communityId, gzhPayUrl, params);

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
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_ERROR, resMap.get("等待用户支付中"));
        }
    }

    public ResultVo checkPayFinish(String communityId, String orderNum) {
        Map<String, String> result = null;
        String mchtNo_SM4 = CommunitySettingFactory.getValue(communityId, "mchtNo_SM4");
        String productNo_SM4 = CommunitySettingFactory.getValue(communityId, "productNo_SM4");
        String publicKey_SM4 = CommunitySettingFactory.getValue(communityId, "publicKey_SM4");
        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_SM4);// 收款商户编号
        params.put("tran_no", orderNum);// 商户流水
        params.put("txn_no", "");// 支付流水

        // 对准备加签参数排序
        String decryParams = EncryptDecryptFactory.execute(communityId, queryUrl, params);

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
            return new ResultVo(ResultVo.CODE_OK, "成功");
        } else {
            return new ResultVo(ResultVo.CODE_WAIT_PAY, "等待支付完成");
        }
    }
}
