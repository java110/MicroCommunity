package com.java110.acct.payment.adapt.spdb;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.util.HashMap;
import java.util.Map;

/**
 * SM2密钥对生成工具
 *
 * @author z
 **/
public class SM2Util {

    private SM2Util() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 私钥长度
     */
    private static final int KEY_PRIVATE_LENGTH = 66;

    public static Map<String, String> generateKey() {
        SM2 sm2 = SmUtil.sm2();
        byte[] privateKey = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        byte[] publicKey = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
        String privateKeyStr = HexUtil.encodeHexStr(privateKey);
        if (privateKeyStr.length() == KEY_PRIVATE_LENGTH) {
            Map<String, String> result = new HashMap<>(1);
            result.put("privateKey", privateKeyStr);
            result.put("publicKey", HexUtil.encodeHexStr(publicKey));
            return result;
        } else {
            return generateKey();
        }
    }
}
