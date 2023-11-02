package com.java110.acct.payment.adapt.easypay.utils;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类
 *
 * @Date: 2021/10/14 20:39
 * @Author: pandans
 */
public class RsaUtil {
    private static final String ALGORITHM = "RSA";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM_SHA256 = "SHA256withRSA";

    /**
     * @param content:   待加密内容
     * @param publicKey: 加密公钥
     * @return 加密过的内容
     * @Description 使用公钥进行你内容假面
     * @date: 2021/10/17 11:22
     * @author: pandans
     */
    public static String encryptWithPKCS1(String content, String publicKey) {
        if (publicKey == null || "".equals(publicKey)) {
            //缺少Rsa密钥信息
            return null;
        }
        try {
            PublicKey pubKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] output = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

            return Base64.encode2String(output);
        } catch (Exception e) {
            //签名失败
            return null;
        }
    }

    /**
     * @param content:    已加密内容
     * @param privateKey: 加密公钥
     * @return 解密的内容
     * @Description 使用私钥进行解密
     * @date: 2021/10/17 11:22
     * @author: pandans
     */
    public static String decryptWithPKCS1(String content, String privateKey) {
        if (privateKey == null || "".equals(privateKey)) {
            //缺少Rsa密钥信息
            return null;
        }
        try {
            PrivateKey priKey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);

            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] output = cipher.doFinal(Base64.decode2Byte(content));

            return new String(output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            //签名失败
            return null;
        }
    }

    /**
     * @param content    :
     * @param privateKey : 易生提供的私钥
     * @return java.lang.String
     * @Description:
     * @Date: 2021/10/14 20:34
     * @Author: pandans
     */
    public static String signBySHA256WithRSA(String content, String privateKey) {
        if (privateKey == null || "".equals(privateKey)) {
            //缺少签名私钥
            return null;
        }
        try {
            PrivateKey priKey = getPrivateKey(privateKey);

            Signature signature = Signature.getInstance(ALGORITHM_SHA256);
            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encode2String(signature.sign());
        } catch (Exception e) {
            //签名失败
            return null;
        }
    }


    /**
     * @param content:   签名数据内容
     * @param sign:      签名
     * @param publicKey: 易生提供的公钥
     * @return boolean
     * @Description 验签
     * @Date: 2021/10/14 21:08
     * @Author: pandans
     */
    public static boolean verifyBySHA256WithRSA(String content, String sign, String publicKey) {
        //1）	使用正則表達式把應答報文分為兩部分：JSON格式的報文(request/response)部分和簽名(signature)部分，注意為保證原文順序，不能轉為JSON對象去操作；
        //2）	使用SHA256算法對JSON格式的報文(request/response)部分獲取消息摘要；
        //3）	使用公鑰將簽名解碼為消息摘要；
        //4）	比較第2，3步驟的消息摘要，如果相同，說明原文沒有變化，驗證通過；
        if (publicKey == null || "".equals(publicKey)) {
            //缺少签名私钥
            return false;
        }
        try {
            PublicKey pubKey = getPublicKey(publicKey);

            Signature signature = Signature.getInstance(ALGORITHM_SHA256);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.decode2Byte(sign));
        } catch (Exception e) {
            //验签失败
            return false;
        }
    }

    public static RSAPublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode2Byte(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode2Byte(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }
}