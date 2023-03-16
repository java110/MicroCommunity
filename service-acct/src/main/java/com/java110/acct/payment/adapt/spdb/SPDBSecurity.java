package com.java110.acct.payment.adapt.spdb;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.nio.charset.StandardCharsets;

/**
 * 加解密及加签验签
 *
 * @author z
 */
public class SPDBSecurity {

    private static final String ERROR_KEY = "初始化国密类异常，请检查密钥参数";
    private static final String ERROR_SM2 = "加签异常，请检查SM2私钥";
    private static final String ERROR_SECRET = "普通验签加签异常，请检查secret参数";

    /**
     * API平台APP唯一标识
     */
    private final String clientId;

    /**
     * sm4加解密对象
     */
    private final SymmetricCrypto sm4;
    /**
     * sm2加签对象
     */
    private final SM2 signSM2;
    /**
     * sm2验签对象
     */
    private final SM2 verifySM2;

    /**
     * 构造方法，初始化
     *
     * @param secret           secret
     * @param sm2PrivateKey    合作方国密私钥
     * @param spdbSM2PublicKey 浦发国密公钥
     */
    public SPDBSecurity(String clientId, String secret, String sm2PrivateKey, String spdbSM2PublicKey) throws SPDBNormalException {
        try {
            this.clientId = clientId;

            String sha256Key = SecureUtil.sha256(secret);
            SM3 sm3 = SmUtil.sm3();
            String sm3Key = sm3.digestHex(sha256Key);
            String md5Key = SecureUtil.md5(sm3Key);
            this.sm4 = SmUtil.sm4(HexUtil.decodeHex(md5Key));

            this.signSM2 = new SM2(sm2PrivateKey, null, null);
            this.verifySM2 = new SM2(null, ECKeyUtil.toSm2PublicParams(spdbSM2PublicKey));
        } catch (Exception e) {
            throw new SPDBNormalException(ERROR_KEY, e);
        }
    }

    /**
     * 构造方法，初始化
     */
    public SPDBSecurity(SPDBApp spdbApp) throws SPDBNormalException {
        try {
            this.clientId = spdbApp.getClientId();

            String sha256Key = SecureUtil.sha256(spdbApp.getSecret());
            SM3 sm3 = SmUtil.sm3();
            String sm3Key = sm3.digestHex(sha256Key);
            String md5Key = SecureUtil.md5(sm3Key);
            this.sm4 = SmUtil.sm4(HexUtil.decodeHex(md5Key));

            this.signSM2 = new SM2(spdbApp.getSm2PrivateKey(), null, null);
            this.verifySM2 = new SM2(null, ECKeyUtil.toSm2PublicParams(spdbApp.getSpdbSM2PublicKey()));
        } catch (Exception e) {
            throw new SPDBNormalException(ERROR_KEY, e);
        }
    }


    /**
     * 浦发sm4加密
     *
     * @param reqBody 请求报文明文
     * @return 密文
     */
    public String encrypt(String reqBody) {
        return Base64.encode(sm4.encryptHex(reqBody).toLowerCase());
    }

    /**
     * 浦发sm4解密
     *
     * @param encryptResBody 响应报文密文
     * @return 明文
     */
    public String decrypt(String encryptResBody) {
        return sm4.decryptStr(Base64.decodeStr(encryptResBody).toLowerCase(), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 请求加签
     *
     * @param reqBody 请求报文明文
     * @return 加签结果
     */
    public String sign(String reqBody) throws SPDBNormalException {
        try {
            byte[] sign = signSM2.sign(reqBody.getBytes(StandardCharsets.UTF_8));
            return Base64.encode(HexUtil.encodeHexStr(sign));
        } catch (Exception e) {
            throw new SPDBNormalException(ERROR_SM2, e);
        }
    }

    /**
     * 响应验签
     *
     * @param resBody 响应报文明文
     * @param resSign 响应头浦发签名
     * @return 是否验签通过
     */
    public boolean verifySign(String resBody, String resSign) {
        byte[] body = Base64.encode(SecureUtil.sha1().digest(resBody)).getBytes(StandardCharsets.UTF_8);
        byte[] spdbSign = HexUtil.decodeHex(Base64.decodeStr(resSign));
        return verifySM2.verify(body, spdbSign);
    }

    /**
     * 普通验签接入方式加签
     *
     * @param reqBody 请求报文明文
     * @return 加签结果
     */
    public String signNormal(String reqBody) throws SPDBNormalException {
        try {
            String sha1 = SecureUtil.sha1(reqBody);
            String dataEncrypt = Base64.encode(HexUtil.decodeHex(sha1));
            SM3 sm3 = SmUtil.sm3();
            String sm3Key = sm3.digestHex(dataEncrypt);
            return Base64.encode(sm4.encryptHex(sm3Key).toUpperCase());
        } catch (Exception e) {
            throw new SPDBNormalException(ERROR_SECRET, e);
        }
    }

    public String signNormalWithByte(byte[] data) throws SPDBNormalException {
        try {
            byte[] sha1 = SecureUtil.sha1().digest(data);
            String dataEncrypt = Base64.encode(sha1);
            SM3 sm3 = SmUtil.sm3();
            String sm3Key = sm3.digestHex(dataEncrypt);
            return Base64.encode(sm4.encryptHex(sm3Key).toUpperCase());
        } catch (Exception e) {
            throw new SPDBNormalException(ERROR_SECRET, e);
        }
    }


    public String getClientId() {
        return clientId;
    }
}
