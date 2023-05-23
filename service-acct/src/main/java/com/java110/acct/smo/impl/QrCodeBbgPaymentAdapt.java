package com.java110.acct.smo.impl;

import com.alibaba.fastjson.JSONObject;
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

    private static String gzhPayUrl = "https://epaytest.bankofbbg.com/www/corepaycer/ScanCodePay";

    private static String queryUrl = "https://epaytest.bankofbbg.com/www/corepaycer/QueryTxnInfo";// 交易查询地址

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

        String mchtNo_RSA2 = CommunitySettingFactory.getValue(communityId, "mchtNo_RSA2");
        String productNo_RSA2 = CommunitySettingFactory.getValue(communityId, "productNo_RSA2");
        String opToken_RSA2 = CommunitySettingFactory.getValue(communityId, "opToken_RSA2");
        String mcht_PrivateKey_RSA2 = CommunitySettingFactory.getRemark(communityId, "mcht_PrivateKey_RSA2");
        String bank_PublicKey_RSA2 = CommunitySettingFactory.getRemark(communityId, "bank_PublicKey_RSA2");

        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", mchtNo_RSA2);// 收款商户编号
        params.put("product_no", productNo_RSA2);// 产品编号
        params.put("tran_no", orderNum);// 商户流水
        params.put("auth_code", authCode);// 码类型
        params.put("amt", payAmount);// 交易金额
        params.put("ware_name", feeName);// 商品名称
        params.put("device_ip", "172.0.0.1");// 商户数据包
        params.put("recog_no", "123123");// 交易终端编号

        String json = JsonUtil.mapToJson(params);
        System.out.println("加密前：" + json);

        Map<String, Object> soreMap = JsonUtil.sortMapByKey(params);

        // 开始加密
        byte[] en = AesEncrypt.encryptByte(json, "utf-8", opToken_RSA2);
        if (en == null || en.length <= 0) {
            System.err.println("加密失败");
            throw new IllegalArgumentException("加密失败");
        }
        String encryptBase64Str = AesEncrypt.parseByte2HexStr(en);
        System.out.println("加密后：" + encryptBase64Str);

        String signtBase64Str = CAUtil.rsa256Sign(json, "utf-8", mcht_PrivateKey_RSA2);
        System.out.println("加签串：" + signtBase64Str);

        Map<String, Object> signParams = new HashMap<>();
        signParams.put("mcht_no", mchtNo_RSA2);// 收款商户编号
        signParams.put("sign_type", SIGN_TYPE);
        signParams.put("sign", signtBase64Str);
        signParams.put("enc_data", encryptBase64Str);// 加密后请求参数

        String requestParams = JsonUtil.mapToJson(signParams);
        System.out.println("最终请求参数：" + requestParams);
        System.err.println("");
        String returnResult = HttpRequestUtil.httpPost(gzhPayUrl, requestParams);
        System.out.println("支付结果返回值(原文):" + returnResult);
        if (returnResult == null) {
            System.err.println("通道响应异常");
            throw new IllegalArgumentException("通道响应异常");
        }
        // 开始解密
        Map<String, Object> responseParams = JsonUtil.jsonToMap(returnResult);
        // 开始解密
        String decryptBase64Str = (String) responseParams.get("enc_data");
        String verifyBase64Str = (String) responseParams.get("sign");
        byte[] bt = AesEncrypt.parseHexStr2Byte(decryptBase64Str);
        byte[] decrypt = AesEncrypt.decryptByte(bt, opToken_RSA2);
        if (decrypt == null) {
            System.err.println("解密失败");
            throw new IllegalArgumentException("解密失败");
        }
        boolean isSuccess = CAUtil.rsa256Verify(decrypt, verifyBase64Str, bank_PublicKey_RSA2);
        System.out.println("数据验签：" + isSuccess);
        if (!isSuccess) {
            System.err.println("验签失败");
            throw new IllegalArgumentException("验签失败");
        }
        String decryParams = new String(decrypt);
        System.out.println("支付结果返回值(解密后):" + decryParams);

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
        String mchtNo_RSA2 = CommunitySettingFactory.getValue(communityId, "mchtNo_RSA2");
        String productNo_RSA2 = CommunitySettingFactory.getValue(communityId, "productNo_RSA2");
        String opToken_RSA2 = CommunitySettingFactory.getValue(communityId, "opToken_RSA2");
        String mcht_PrivateKey_RSA2 = CommunitySettingFactory.getRemark(communityId, "mcht_PrivateKey_RSA2");
        String bank_PublicKey_RSA2 = CommunitySettingFactory.getRemark(communityId, "bank_PublicKey_RSA2");
        Map<String, Object> params = new HashMap<>();
        params.put("version", VERSION);// 版本号 1.0
        params.put("mcht_no", orderNum);// 收款商户编号
        params.put("tran_no", orderNum);// 商户流水
        params.put("txn_no", "");// 支付流水

        // 对准备加签参数排序
        Map<String, Object> soreMap = JsonUtil.sortMapByKey(params);
        // 格式为json
        String json = JsonUtil.mapToJson(soreMap);
        System.out.println("加密前：" + json);
        // 开始加密
        byte[] en = new byte[0];
        try {
            en = AesEncrypt.encryptByte(json, "utf-8", opToken_RSA2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (en == null || en.length <= 0) {
            System.err.println("加密失败");
            throw new IllegalArgumentException("加密失败");
        }
        String encryptBase64Str = AesEncrypt.parseByte2HexStr(en);
        System.out.println("加密后：" + encryptBase64Str);

        String signtBase64Str = null;
        try {
            signtBase64Str = CAUtil.rsa256Sign(json, "utf-8", mcht_PrivateKey_RSA2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("加签串：" + signtBase64Str);

        Map<String, Object> signParams = new HashMap<>();
        signParams.put("mcht_no", mchtNo_RSA2);// 收款商户编号
        signParams.put("sign_type", SIGN_TYPE);
        signParams.put("sign", signtBase64Str);
        signParams.put("enc_data", encryptBase64Str);// 加密后请求参数

        String requestParams = JsonUtil.mapToJson(signParams);
        System.out.println("最终请求参数：" + requestParams);
        System.err.println("");
        String returnResult = null;
        try {
            returnResult = HttpRequestUtil.httpPost(queryUrl, requestParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("支付结果返回值(原文):" + returnResult);
        if (returnResult == null) {
            System.err.println("通道响应异常");
            throw new IllegalArgumentException("通道响应异常");
        }
        // 开始解密
        Map<String, Object> responseParams = JsonUtil.jsonToMap(returnResult);
        // 开始解密
        String decryptBase64Str = (String) responseParams.get("enc_data");
        String verifyBase64Str = (String) responseParams.get("sign");
        byte[] bt = AesEncrypt.parseHexStr2Byte(decryptBase64Str);
        byte[] decrypt = new byte[0];
        try {
            decrypt = AesEncrypt.decryptByte(bt, opToken_RSA2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (decrypt == null) {
            System.err.println("解密失败");
            throw new IllegalArgumentException("解密失败");
        }
        boolean isSuccess = false;
        try {
            isSuccess = CAUtil.rsa256Verify(decrypt, verifyBase64Str, bank_PublicKey_RSA2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("数据验签：" + isSuccess);
        if (!isSuccess) {
            System.err.println("验签失败");
            throw new IllegalArgumentException("验签失败");
        }
        String decryParams = new String(decrypt);
        System.out.println("支付结果返回值(解密后):" + decryParams);

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
