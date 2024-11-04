package com.java110.acct.payment.adapt.bbgpay;

import com.java110.acct.payment.adapt.bbgpay.lib.GmUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.HttpRequestUtil;
import com.java110.acct.payment.adapt.bbgpay.lib.JsonUtil;
import com.java110.dto.payment.PaymentPoolValueDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptDecryptFactory {


    public static String execute(List<PaymentPoolValueDto> paymentPoolValueDtos, String url, Map<String, Object> params){
        String decrypt = "";
        try {
            String mchtNo_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "mchtNo_SM4");
            String publicKey_SM4 = PaymentPoolValueDto.getValue(paymentPoolValueDtos, "publicKey_SM4");
            // 格式为json
            String json = JsonUtil.mapToJson(params);
            System.out.println("加密前：" + json);
            // 报文加密
            String secretKey = GmUtil.generateSm4Key();
            String encrypt = GmUtil.encryptSm4(json, secretKey);
            System.out.println("加密后：" + encrypt);

            Map<String, Object> signParams = new HashMap<>();
            signParams.put("mcht_no", mchtNo_SM4);// 收款商户编号
            signParams.put("sign_type", "SM4");
            signParams.put("message_key", GmUtil.encryptSm2(secretKey, publicKey_SM4));// 密钥加密
            signParams.put("enc_data", encrypt);// 加密后请求参数

            String requestParams = JsonUtil.mapToJson(signParams);
            System.out.println("最终请求参数：" + requestParams);
            System.err.println("");
            String returnResult = HttpRequestUtil.httpPost(url, requestParams);
            System.out.println("支付结果返回值(原文):" + returnResult);
            if (returnResult == null) {
                System.err.println("通道响应异常");
                throw new IllegalArgumentException("通道响应异常");

            }
            // 开始解密
            Map<String, Object> responseParams = JsonUtil.jsonToMap(returnResult);
            if (!responseParams.containsKey("enc_data")) {
                System.err.println("交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message"));
                throw new IllegalArgumentException("交易失败-->" + responseParams.get("return_code") + ":" + responseParams.get("return_message"));
            }
            String decryptStr = (String) responseParams.get("enc_data");

             decrypt = GmUtil.decryptSm4(decryptStr, secretKey);
            if (decrypt == null) {
                System.err.println("解密失败");
                throw new IllegalArgumentException("解密失败");
            }
            System.out.println("支付结果返回值(解密后):" + decrypt);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return decrypt;
    }
}
