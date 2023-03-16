package com.java110.acct.payment.adapt.spdb;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调接口RSA私钥解密、验签工具
 *
 * @author z
 **/
public class RSAUtil {

    private RSAUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * RSA钥对生成
     *
     * @return privateKey私钥 publicKey公钥
     */
    public static Map<String, String> generateKey() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        String privateKey = Base64.encode(pair.getPrivate().getEncoded());
        String publicKey = Base64.encode(pair.getPublic().getEncoded());
        Map<String, String> result = new HashMap<>(1);
        result.put("privateKey", privateKey);
        result.put("publicKey", publicKey);
        return result;
    }

    /**
     * 验证签名
     *
     * @param decryptBody   报文明文
     * @param spdbSign      浦发签名，一般放在请求头的"SPDB_PUBLIC_KEY"字段中
     * @param spdbPublicKey 浦发公钥
     * @return 是否验签通过
     */
    public static boolean verifySign(String decryptBody, String spdbSign, String spdbPublicKey) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, spdbPublicKey);
        return sign.verify(decryptBody.getBytes(StandardCharsets.UTF_8), Base64.decode(spdbSign));
    }

    /**
     * 使用合作方私钥解密报文体
     *
     * @param encryptBody 报文密文
     * @return 解密后的明文
     */
    public static String decryptStr(String encryptBody, String myPrivateKey) {
        RSA rsa = new RSA(myPrivateKey, null);
        return rsa.decryptStr(encryptBody, KeyType.PrivateKey);
    }

}
