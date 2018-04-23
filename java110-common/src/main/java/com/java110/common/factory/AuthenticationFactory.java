package com.java110.common.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.NoAuthorityException;
import com.java110.entity.center.DataFlow;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * 鉴权工厂类
 * Created by wuxw on 2018/4/23.
 */
public class AuthenticationFactory {

    /**
     * md5签名
     * @param inStr
     * @return
     */
    public static String md5(String inStr) throws NoAuthorityException{
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"MD5签名过程中出现错误");
        }
    }

    /**
     * dataFlow 对象签名
     * @param dataFlow
     * @return
     */
    public static String dataFlowMd5(DataFlow dataFlow) throws NoAuthorityException{
        if(dataFlow == null){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"MD5签名过程中出现错误");
        }
        String reqInfo = dataFlow.getTransactionId() + dataFlow.getAppId() + dataFlow.getReqBusiness().toJSONString()+dataFlow.getAppRoutes().get(0).getSecurityCode();
        return md5(reqInfo);
    }

    /**
     * md5加密
     * @param transactionId 流水
     * @param appId 应用ID
     * @param businesses 内容
     * @return
     */
    public static String md5(String transactionId,String appId,String businesses,String code){
        return md5(transactionId+appId+businesses).toLowerCase();
    }

    /**
     * 添加 sign
     * @param dataFlow
     * @param responseJson
     */
    public static void putSign(DataFlow dataFlow,JSONObject responseJson){
        JSONObject orders = responseJson.getJSONObject("orders");
        JSONArray business = responseJson.getJSONArray("business");
        if(dataFlow == null || dataFlow.getAppRoutes() == null || dataFlow.getAppRoutes().size() == 0) {
            /*orders.put("sign", AuthenticationFactory.md5(orders.getString("transactionId"), orders.getString("responseTime"),
                    business.toJSONString(), MappingCache.getValue(MappingConstant.KEY_DEFAULT_SECURITY_CODE)));*/
            orders.put("sign","");
        }else {
            orders.put("sign", AuthenticationFactory.md5(orders.getString("transactionId"), orders.getString("responseTime"),
                    business.toJSONString(), dataFlow.getAppRoutes().get(0).getSecurityCode()));
        }
    }


    /**
     * 加密
     * @param data
     * @param publicKey
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey, int keySize)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int blockSize = (keySize >> 3) - 11;

        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        while (inputLen - offSet > 0) {
            byte[] buf;
            if (inputLen - offSet > blockSize) {
                buf = cipher.doFinal(data, offSet, blockSize);
            }else {
                buf = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(buf, 0, buf.length);
            ++i;
            offSet = i * blockSize;
        }
        byte[] result = out.toByteArray();

        return result;
    }

    /**
     * 解密
     * @param data
     * @param privateKey
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey, int keySize)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int blockSize = keySize >> 3;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[blockSize];
        int len = 0;
        while ((len = byteArrayInputStream.read(buf)) > 0) {
            byteArrayOutputStream.write(cipher.doFinal(buf, 0, len));
        }

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 加载公钥
     * @param keyData
     * @return
     * @throws Exception
     */
    public static PublicKey loadPubKey(String keyData)
            throws Exception
    {
        return loadPemPublicKey(keyData, "RSA");
    }

    /**
     * 加载私钥

     * @param keyData
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String keyData) throws Exception {
        return loadPrivateKeyPkcs8(keyData, "RSA");
    }

    /**
     * 加载私钥
     * @param privateKeyPem
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKeyPkcs8(String privateKeyPem, String algorithm)
            throws Exception
    {
        String privateKeyData = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyData = privateKeyData.replace("-----END PRIVATE KEY-----", "");
        privateKeyData = privateKeyData.replace("\n", "");
        privateKeyData = privateKeyData.replace("\r", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyData.getBytes());

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 加载公钥
     * @param publicPemData
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PublicKey loadPemPublicKey(String publicPemData, String algorithm)
            throws Exception
    {
        String publicKeyPEM = publicPemData.replace("-----BEGIN PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replace("\n", "");
        publicKeyPEM = publicKeyPEM.replace("\r", "");

        byte[] decoded =Base64.getDecoder().decode(publicKeyPEM.getBytes());

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(spec);
    }

    //生成密钥对
    private static KeyPair genKeyPair(int keyLength) throws Exception{
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static void main(String[] args) throws Exception{
        KeyPair keyPair=genKeyPair(1024);

        //获取公钥，并以base64格式打印出来
        PublicKey publicKey=keyPair.getPublic();
        System.out.println("公钥："+new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        //获取私钥，并以base64格式打印出来
        PrivateKey privateKey=keyPair.getPrivate();
        System.out.println("私钥："+new String(Base64.getEncoder().encode(privateKey.getEncoded())));

    }
}


