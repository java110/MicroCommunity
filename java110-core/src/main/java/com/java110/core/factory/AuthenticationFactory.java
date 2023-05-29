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
import com.java110.core.context.ApiDataFlow;
import com.java110.core.context.DataFlow;
import com.java110.dto.reportData.ReportDataDto;
import com.java110.dto.reportData.ReportDataHeaderDto;
import com.java110.utils.cache.JWTCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.NoAuthorityException;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 鉴权工厂类
 * Created by wuxw on 2018/4/23.
 */
public class AuthenticationFactory {

    public final static String PASSWD_SALT = "hc@java110";

    public final static String AES_KEY = "whoisyourdaddy!!";
    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";


    // 加密
    public static String AesEncrypt(String sSrc, String sKey) {
        try {
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
//            if (sKey.length() != 16) {
//                System.out.print("Key长度不是16位");
//                return null;
//            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return Base64Convert.byteToBase64(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 解密
    public static String AesDecrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
//            if (sKey.length() != 16) {
//                System.out.print("Key长度不是16位");
//                return null;
//            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64Convert.base64ToByte(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

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

    public static String SHA1Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception localException) {
        }
        return resultString;
    }


    public static final String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xFF) < 16) {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xFF, 16));
        }
        return buf.toString().toUpperCase();
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
        String url = dataFlow.getRequestHeaders().get("REQUEST_URL");
        String param = "";
        if (url.indexOf("?") > 0) {
            param = url.substring(url.indexOf("?"));
        }
        //,DELETE
        reqInfo += "GET".equals(dataFlow.getRequestHeaders().get(CommonConstant.HTTP_METHOD)) ?
                param : dataFlow.getReqData();
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
     * md5签名
     *
     * @param reportDataDto
     * @return
     */
    public static void authReportDataSign(ReportDataDto reportDataDto, String code) throws NoAuthorityException {
        ReportDataHeaderDto reportDataHeaderDto = reportDataDto.getReportDataHeaderDto();
        if (reportDataHeaderDto == null) {
            throw new IllegalArgumentException("参数错误");
        }
        String newSign = md5(reportDataHeaderDto.getTranId() + reportDataHeaderDto.getReqTime() + reportDataDto.getReportDataBodyDto().toJSONString() + code).toLowerCase();
        if (!newSign.equals(reportDataHeaderDto.getSign())) {
            throw new IllegalArgumentException("签名失败");
        }
    }

    public static void generatorReportDataSign(ReportDataDto reportDataDto, String code) {
        ReportDataHeaderDto reportDataHeaderDto = reportDataDto.getReportDataHeaderDto();
        if (reportDataHeaderDto == null) {
            throw new IllegalArgumentException("参数错误");
        }
        String newSign = md5(reportDataHeaderDto.getTranId() + reportDataHeaderDto.getReqTime() + reportDataDto.getReportDataBodyDto().toJSONString() + code).toLowerCase();
        reportDataHeaderDto.setSign(newSign);
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
//        KeyPair keyPair = genKeyPair(1024);
//
//        //获取公钥，并以base64格式打印出来
//        PublicKey publicKey = keyPair.getPublic();
//        System.out.println("公钥：" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));
//
//        //获取私钥，并以base64格式打印出来
//        PrivateKey privateKey = keyPair.getPrivate();
//        System.out.println("私钥：" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));

        System.out.printf("passwdMd5 " + passwdMd5("397301"));

    }


}


