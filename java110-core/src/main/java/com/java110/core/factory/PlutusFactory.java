package com.java110.core.factory;

import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * AES加密解密算法
 *
 * @author long
 */
public class PlutusFactory {
    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/CBC/PKCS5Padding";

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    //密钥
    public static String SECRET_KEY = "3738597658384d6c316a4758527a5a35";


    // /** 创建密钥 **/
    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        data = hexStringToByteArray(key);
        return new SecretKeySpec(data, "AES");
    }

    /**
     * 实例化私钥
     *
     * @return
     */
    private static PrivateKey getPrivateKey(String priKey) {
        PrivateKey privateKey = null;
//        String priKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDKO3fVJVtKuBlj\n" +
//                "T2HkhjQykS/jKiNuQ4o0IItGJwTv4IX7m+vlKwqPQFylr1POdRX0z4lwFWRWvxCv\n" +
//                "fpORW83W6a6J6LSKfDc5g5h0mhnYdYMdzLawVEM1YqJD5EVRQZHKleMvwkZbLWt0\n" +
//                "bFJ2o4uVDO7bs/ABv6UdAmOlP6K2fUcw14r5nF+sUpXw9v2wCQys3k3djGOQJQFX\n" +
//                "7/aADXsROp0xPFhHVgu18Rtjp7y5ib8bQ1obMmlf+4yThjlAMJQN9sBTOByUXLQw\n" +
//                "VCCL5oYsSs318mnJnTSmkK88pxDYp1Y2K7WQDbZtFiDbNA4bqCecQGbX+6c7NWSr\n" +
//                "dWMn0BsfAgMBAAECggEBAJXh1UKH2U1bfJV59Bemz3Da4h7+0BucuwU/SXnI2YPf\n" +
//                "Z+2+9ep3J/Bbx06UzwwpAwjZ+Aa2FBOmr/shWMVWwQwTTWSwr34j6doaiheBTr56\n" +
//                "+Z5QZuXwzY73d0PSHv3GFwOKa0KuPe69jvJOhh+fvofNegojJjJlkz4Y0zlaHIIa\n" +
//                "ri5iuKM2b5sSeoohCwJF9vmkje9UUpzYgIQhiiLe7jHIj6PP+ILA/+J0IqlqZg7Z\n" +
//                "nZIClUfy1Bn533yxCHvvM2V1gkT4zsmLtgIsJrGP3FHKW0yGj2JKaxI8T4JtjYCX\n" +
//                "QhXoYkzjr+111udD8oe0Tg/8PquFvqi5Cq0rkDqNwsECgYEA5ENMeAuENaG1TKov\n" +
//                "rpXUhmrB1tOcp/BvOLq4KIsgY6/4k2q4MbpK3qXeY90YlLaEsggD8eiEG3RNmGNR\n" +
//                "IbmnqyQBQyk+KYCMTGBTRGqwwjJjTDBkV0hOeLV8CBBcAFGKXLf15HyhuG4xRhvu\n" +
//                "Sq8YcIbvA9fSLkaaYY/pyuoQA3kCgYEA4s5r20Na9WQ5M3QLRY5HMlUb0E0SMSEd\n" +
//                "4UF6mgzpdafDWSsvcOSnJguahZ25DJk12ptKKaJPXxPEa2+u0FN/Jv9KDPkXggXS\n" +
//                "z6yS9E1HfFSbRK6An1Q+34vDahrU5lTJQ7eKYDzb2Xm1gI1VxrxdAN5+Bk3IvH1T\n" +
//                "FUowLOFGFVcCgYAYwqgX2X/05V6qO0fC02PjVM9EA15Z5T3bVH3HgBf6WEtJimkC\n" +
//                "k+etMSbnhEM3Vnkcarwq0IMMC1ijcBqL4vyqFtTAOUgR7mzJmCVQJZaY6ihVSmaI\n" +
//                "BULl2yHiKgwgyLeOiTH2IALW47UamssFdOrcwfZJX27gMC5s6NR+e1dTWQKBgFMu\n" +
//                "qdgM5/s/+sqoMMod2HbZSA4pdhaWssK+pRyCx8zi7n5xnECnW7ZUYyPGKOw59Mps\n" +
//                "UdLbOIkCUvOkTlURinze/GWtpbWGNT79aBg5j5JF5XxXE81btIOAWvc7SAIB7p7r\n" +
//                "XdDWATvNq4euTltJEkMTVt0xAgI/ZI1WXDzZkj6ZAoGBANTu3Ko+x8OgH3WdMSxD\n" +
//                "YTersdTb4j2bj3IkZF8xJemyO7qerEK6H9mkGjSZlR17bQeE3b0RF97GgIw/fIZX\n" +
//                "o6oE7re3EaCp6/PIXTzTPRlLTPNlBvFqDrNurM0YndjAgANa1tDDQ12W8Vtjvv8g\n" +
//                "mX7PvHOnfws522nLZHBz+SzT";

        try {

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(priKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    //实例化公钥
    public static PublicKey getPublicKey(String pubKey) {
        PublicKey publicKey = null;
//        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsIvJ1y1G7/BaWqcq/qVc\n" +
//                "6u7nQb7nuH9vI2MoJc2H9ZGVD27oOIPkEDy7kIiteIaq5lrj6Z8VpG4n84MycsC7\n" +
//                "/7AVScV238pdBkQM/vtm6j3jJsh7dcAU/ngMTzusUgFKlUhClR4uztQM+/obIcAl\n" +
//                "wDlGnY/Nw5XbmzE6igcLgAZLkYq54hfJSG7EyctonL8Q8SPn51eEy9TMh3jju/RH\n" +
//                "KeZzpJ5mYTFzqGU798rzv6r9uBKC/lZvuQcQwK7li4ctINA3EPmRbiLwzLZnTZBf\n" +
//                "h7AmtTMqM2NYrn6Co23NQYLdg0WPSPv1Sxj69BSJ1q62boT2gOO3rsxaK8FN3EJb\n" +
//                "sQIDAQAB";

        try {
            X509EncodedKeySpec PubKeySpec = new X509EncodedKeySpec(
                    Base64.decode(pubKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(PubKeySpec);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (InvalidKeySpecException e1) {
            e1.printStackTrace();
        }
        return publicKey;
    }


    // /** 加密字节数据 **/
    public static byte[] encrypt(byte[] content, String password) {
        try {
            byte[] data = hexStringToByteArray(password);
            SecretKeySpec key = new SecretKeySpec(data, "AES");
            Cipher cipher = Cipher.getInstance(CipherMode);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            byte[] data = hexStringToByteArray(password);
            SecretKeySpec key = new SecretKeySpec(data, "AES");
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String Encryption(String url,String priKey,String secretKey,String devId) {
        JSONObject object = new JSONObject();
        try {
            byte[] b = url.getBytes("UTF-8");
            //AES加密
            byte[] text = PlutusFactory.encrypt(b, secretKey);
            String content = Base64.toBase64String(text);

            //签名
            byte[] sign = PlutusFactory.sign256(text, getPrivateKey(priKey));
            String signature = Base64.toBase64String(sign);

            object.put("devId", devId);
            object.put("content", content);
            object.put("signature", signature);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return object.toString();
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return b;
    }

    //SHA256withRSA签名
    public static byte[] sign256(byte[] data, PrivateKey privateKey) {
        byte[] signed = null;
        Signature signature = null;
        try {
            signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data);
            signed = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return signed;
    }


    //SHA256withRSA验签
    public static boolean verify256(String data, byte[] sign,String pubKey) {
        if (data == null || sign == null) {
            return false;
        }
        try {
            Signature signetcheck = Signature.getInstance(SIGNATURE_ALGORITHM);
            signetcheck.initVerify(getPublicKey(pubKey));
            signetcheck.update(Base64.decode(data));
            return signetcheck.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }

    public static String post(String path,String post){
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(15000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "请求失败";
    }
}
