package com.java110.core.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.NoAuthorityException;
import com.java110.utils.util.StringUtil;

import com.java110.core.context.ApiDataFlow;
import com.java110.core.context.DataFlow;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 鉴权工厂类
 * Created by wuxw on 2018/4/23.
 */
public class AuthenticationFactory {

    private final static String PASSWD_SALT = "hc@java110";

    /**
     * 用户密码 md5签名
     *
     * @param inStr
     * @return
     */
    public static String passwdMd5(String inStr) throws NoAuthorityException {
        return md5(md5(inStr + PASSWD_SALT));
    }

    /**
     * md5签名
     *
     * @param inStr
     * @return
     */
    public static String md5(String inStr) throws NoAuthorityException {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "MD5签名过程中出现错误");
        }
    }

    /**
     * dataFlow 对象签名
     *
     * @param dataFlow
     * @return
     */
    public static String dataFlowMd5(DataFlow dataFlow) throws NoAuthorityException {
        if (dataFlow == null) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "MD5签名过程中出现错误");
        }
        String reqInfo = dataFlow.getTransactionId() + dataFlow.getAppId();
        reqInfo += ((dataFlow.getReqBusiness() == null || dataFlow.getReqBusiness().size() == 0)
                ? dataFlow.getReqData() : dataFlow.getReqBusiness().toJSONString());
        reqInfo += dataFlow.getAppRoutes().get(0).getSecurityCode();
        return md5(reqInfo);
    }

    /**
     * dataFlow 对象签名
     *
     * @param dataFlow
     * @return
     */
    public static String apiDataFlowMd5(ApiDataFlow dataFlow) throws NoAuthorityException {
        if (dataFlow == null) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "MD5签名过程中出现错误");
        }
        String reqInfo = dataFlow.getTransactionId() + dataFlow.getRequestTime() + dataFlow.getAppId();
        //,DELETE
        reqInfo += "GET".equals(dataFlow.getRequestHeaders().get(CommonConstant.HTTP_METHOD)) ?
                dataFlow.getRequestHeaders().get("REQUEST_URL") : dataFlow.getReqData();
        reqInfo += dataFlow.getAppRoutes().get(0).getSecurityCode();
        return md5(reqInfo);
    }

    /**
     * md5加密
     *
     * @param transactionId 流水
     * @param appId         应用ID
     * @param businesses    内容
     * @return
     */
    public static String md5(String transactionId, String appId, String businesses, String code) {
        return md5(transactionId + appId + businesses + code).toLowerCase();
    }

    /**
     * 添加 sign
     *
     * @param dataFlow
     * @param responseJson
     */
    public static void putSign(DataFlow dataFlow, JSONObject responseJson) {
        JSONObject orders = responseJson.getJSONObject("orders");
        JSONArray business = responseJson.getJSONArray("business");
        if (dataFlow == null || dataFlow.getAppRoutes() == null || dataFlow.getAppRoutes().size() == 0 || StringUtil.isNullOrNone(dataFlow.getAppRoutes().get(0).getSecurityCode())) {
            /*orders.put("sign", AuthenticationFactory.md5(orders.getString("transactionId"), orders.getString("responseTime"),
                    business.toJSONString(), MappingCache.getValue(MappingConstant.KEY_DEFAULT_SECURITY_CODE)));*/
            orders.put("sign", "");
        } else {
            orders.put("sign", AuthenticationFactory.md5(orders.getString("transactionId"), orders.getString("responseTime"),
                    business == null ? "" : business.toJSONString(), dataFlow.getAppRoutes().get(0).getSecurityCode()));
        }
    }

    /**
     * 添加 sign
     *
     * @param dataFlow
     * @param headers
     */
    public static void putSign(DataFlow dataFlow, Map<String, String> headers) {
        if (dataFlow == null || dataFlow.getAppRoutes() == null || dataFlow.getAppRoutes().size() == 0 || StringUtil.isNullOrNone(dataFlow.getAppRoutes().get(0).getSecurityCode())) {
            headers.put("resSign", "");
        } else {
            headers.put("resSign", AuthenticationFactory.md5(dataFlow.getTransactionId(), headers.get("responseTime"),
                    dataFlow.getResData(), dataFlow.getAppRoutes().get(0).getSecurityCode()));
        }
    }


    /**
     * 加密
     *
     * @param data
     * @param publicKey
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey, int keySize)
            throws Exception {
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
            } else {
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
     *
     * @param data
     * @param privateKey
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey, int keySize)
            throws Exception {
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
     *
     * @param keyData
     * @return
     * @throws Exception
     */
    public static PublicKey loadPubKey(String keyData)
            throws Exception {
        return loadPemPublicKey(keyData, "RSA");
    }

    /**
     * 加载私钥
     *
     * @param keyData
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String keyData) throws Exception {
        return loadPrivateKeyPkcs8(keyData, "RSA");
    }

    /**
     * 加载私钥
     *
     * @param privateKeyPem
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKeyPkcs8(String privateKeyPem, String algorithm)
            throws Exception {
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
     *
     * @param publicPemData
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PublicKey loadPemPublicKey(String publicPemData, String algorithm)
            throws Exception {
        String publicKeyPEM = publicPemData.replace("-----BEGIN PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        publicKeyPEM = publicKeyPEM.replace("\n", "");
        publicKeyPEM = publicKeyPEM.replace("\r", "");

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM.getBytes());

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(spec);
    }

    //生成密钥对
    private static KeyPair genKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 用户密码
     *
     * @param userPwd
     * @return
     */
    public static String md5UserPassword(String userPwd) {
        String userPasswordSecret = MappingCache.getValue(MappingConstant.KEY_USER_PASSWORD_SECRET);
        if (StringUtil.isNullOrNone(userPasswordSecret)) {
            userPasswordSecret = CommonConstant.DEFAULT_USER_PWD_SECRET;
        }
        return md5(md5(userPwd + userPasswordSecret));
    }

    /**
     * 创建token
     *
     * @return
     */
    public static String createAndSaveToken(Map<String, String> info) throws Exception {

        if (!info.containsKey(CommonConstant.LOGIN_USER_ID)) {
            throw new InvalidParameterException("参数中没有包含：" + CommonConstant.LOGIN_USER_ID);
        }

        String jdi = UUID.randomUUID().toString().replace("-", "");
        String jwtSecret = MappingCache.getValue(MappingConstant.KEY_JWT_SECRET);
        if (StringUtil.isNullOrNone(jwtSecret)) {
            jwtSecret = CommonConstant.DEFAULT_JWT_SECRET;
        }
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTCreator.Builder jwt = JWT.create();
        for (String key : info.keySet()) {
            if (CommonConstant.LOGIN_USER_ID.equals(key)) {
                continue;
            }
            jwt.withClaim(key, info.get(key));
        }
        String expireTime = MappingCache.getValue(MappingConstant.KEY_JWT_EXPIRE_TIME);
        if (StringUtil.isNullOrNone(expireTime)) {
            expireTime = CommonConstant.DEFAULT_JWT_EXPIRE_TIME;
        }
        //保存token Id
        JWTCache.setValue(jdi, info.get(CommonConstant.LOGIN_USER_ID), Integer.parseInt(expireTime));
        jwt.withIssuer("java110");
        jwt.withJWTId(jdi);
        return jwt.sign(algorithm);
    }

    /**
     * 删除Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static void deleteToken(String token) throws Exception {
        String jwtSecret = MappingCache.getValue(MappingConstant.KEY_JWT_SECRET);
        if (StringUtil.isNullOrNone(jwtSecret)) {
            jwtSecret = CommonConstant.DEFAULT_JWT_SECRET;
        }
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("java110").build();
        DecodedJWT jwt = verifier.verify(token);
        String jdi = jwt.getId();
        //保存token Id
        String userId = JWTCache.getValue(jdi);
        if (!StringUtil.isNullOrNone(userId)) { //说明redis中jdi 已经失效
            JWTCache.removeValue(jdi);
        }
    }

    /**
     * 校验Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, String> verifyToken(String token) throws Exception {
        String jwtSecret = MappingCache.getValue(MappingConstant.KEY_JWT_SECRET);
        if (StringUtil.isNullOrNone(jwtSecret)) {
            jwtSecret = CommonConstant.DEFAULT_JWT_SECRET;
        }
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("java110").build();
        DecodedJWT jwt = verifier.verify(token);
        String jdi = jwt.getId();
        //保存token Id
        String userId = JWTCache.getValue(jdi);
        if (StringUtil.isNullOrNone(userId)) {
            throw new JWTVerificationException("用户还未登录");
        }
        String expireTime = MappingCache.getValue(MappingConstant.KEY_JWT_EXPIRE_TIME);
        if (StringUtil.isNullOrNone(expireTime)) {
            expireTime = CommonConstant.DEFAULT_JWT_EXPIRE_TIME;
        }
        //刷新过时时间
        JWTCache.resetExpireTime(jdi, Integer.parseInt(expireTime));
        Map<String, Claim> claims = jwt.getClaims();
        // Add the claim to request header
        Map<String, String> paramOut = new HashMap<String, String>();
        for (String key : claims.keySet()) {
            paramOut.put(key, claims.get(key).asString());
        }
        paramOut.put(CommonConstant.LOGIN_USER_ID, userId);
        return paramOut;
    }


    /***********************************JWT start***************************************/


    /***********************************JWT end***************************************/
    public static void main(String[] args) throws Exception {
        KeyPair keyPair = genKeyPair(1024);

        //获取公钥，并以base64格式打印出来
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("公钥：" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        //获取私钥，并以base64格式打印出来
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("私钥：" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));

    }
}


