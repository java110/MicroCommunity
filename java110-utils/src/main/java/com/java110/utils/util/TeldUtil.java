package com.java110.utils.util;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeldUtil {
    private static Map<String, String> map = new ConcurrentHashMap<String, String>(3);
    // 加密
    public static String Encrypt(String sSrc, String sKey, String ivStr) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度1234567890123456
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        // sSrc= escapeChar(sSrc);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String str = encoder.encodeToString(encrypted);
        str = str.replaceAll("\r\n", "");
        str = str.replaceAll("\n", "");

        return str;
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey, String ivStr) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(sSrc);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                // originalString= unEscapeChar(originalString);
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
     * 计算参数的md5信息
     *
     * @param str 待处理的字节数组
     * @return md5摘要信息
     * @throws NoSuchAlgorithmException
     */
    private static byte[] md5(byte[] str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str);
        return md.digest();
    }

    /**
     * 将待加密数据data，通过密钥key，使用hmac-md5算法进行加密，然后返回加密结果。 参照rfc2104 HMAC算法介绍实现。
     *
     * @param key  密钥
     * @param data 待加密数据
     * @return 加密结果
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getHmacMd5Bytes(byte[] key, byte[] data)
            throws NoSuchAlgorithmException {
        /*
         * HmacMd5 calculation formula: H(K XOR opad, H(K XOR ipad, text))
         * HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
         * H代表hash算法，本类中使用MD5算法，K代表密钥，text代表要加密的数据 ipad为0x36，opad为0x5C。
         */
        int length = 64;
        byte[] ipad = new byte[length];
        byte[] opad = new byte[length];
        for (int i = 0; i < 64; i++) {
            ipad[i] = 0x36;
            opad[i] = 0x5C;
        }
        byte[] actualKey = key; // Actual key.
        byte[] keyArr = new byte[length]; // Key bytes of 64 bytes length
        /*
         * If key's length is longer than 64,then use hash to digest it and use
         * the result as actual key. 如果密钥长度，大于64字节，就使用哈希算法，计算其摘要，作为真正的密钥。
         */
        if (key.length > length) {
            actualKey = md5(key);
        }
        for (int i = 0; i < actualKey.length; i++) {
            keyArr[i] = actualKey[i];
        }
        /*
         * append zeros to K 如果密钥长度不足64字节，就使用0x00补齐到64字节。
         */
        if (actualKey.length < length) {
            for (int i = actualKey.length; i < keyArr.length; i++)
                keyArr[i] = 0x00;
        }

        /*
         * calc K XOR ipad 使用密钥和ipad进行异或运算。
         */
        byte[] kIpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kIpadXorResult[i] = (byte) (keyArr[i] ^ ipad[i]);
        }

        /*
         * append "text" to the end of "K XOR ipad" 将待加密数据追加到K XOR ipad计算结果后面。
         */
        byte[] firstAppendResult = new byte[kIpadXorResult.length + data.length];
        for (int i = 0; i < kIpadXorResult.length; i++) {
            firstAppendResult[i] = kIpadXorResult[i];
        }
        for (int i = 0; i < data.length; i++) {
            firstAppendResult[i + keyArr.length] = data[i];
        }

        /*
         * calc H(K XOR ipad, text) 使用哈希算法计算上面结果的摘要。
         */
        byte[] firstHashResult = md5(firstAppendResult);

        /*
         * calc K XOR opad 使用密钥和opad进行异或运算。
         */
        byte[] kOpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kOpadXorResult[i] = (byte) (keyArr[i] ^ opad[i]);
        }

        /*
         * append "H(K XOR ipad, text)" to the end of "K XOR opad" 将H(K XOR
         * ipad, text)结果追加到K XOR opad结果后面
         */
        byte[] secondAppendResult = new byte[kOpadXorResult.length
                + firstHashResult.length];
        for (int i = 0; i < kOpadXorResult.length; i++) {
            secondAppendResult[i] = kOpadXorResult[i];
        }
        for (int i = 0; i < firstHashResult.length; i++) {
            secondAppendResult[i + keyArr.length] = firstHashResult[i];
        }

        /*
         * H(K XOR opad, H(K XOR ipad, text)) 对上面的数据进行哈希运算。
         */
        byte[] hmacMd5Bytes = md5(secondAppendResult);
        return hmacMd5Bytes;
    }

    public static String getHmacMd5Str(String key, String data) {
        String result = "";
        try {
            byte[] keyByte = key.getBytes("UTF-8");
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] hmacMd5Byte = getHmacMd5Bytes(keyByte, dataByte);
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < hmacMd5Byte.length; i++) {
                if (Integer.toHexString(0xFF & hmacMd5Byte[i]).length() == 1)
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & hmacMd5Byte[i]));
                else
                    md5StrBuff.append(Integer
                            .toHexString(0xFF & hmacMd5Byte[i]));
            }
            result = md5StrBuff.toString().toUpperCase();

        } catch (Exception e) {
            // logger.error("error getHmacMd5Str()",e);
            e.printStackTrace();
        }
        return result;

    }


    /**
     * 生成加密报文
     *
     * @param jsonParam
     * @param aesKey
     * @param aesIV
     * @param signKey
     * @param OPERATOR_ID
     * @return
     * @throws Exception
     */
    public static String generateSecurityParam(String jsonParam, String aesKey, String aesIV, String signKey, String OPERATOR_ID)
            throws Exception {
        System.out.println("generateSecurityParam=" + jsonParam);
        JSONObject paramObj = new JSONObject();
        paramObj.put("OperatorID", OPERATOR_ID);
        paramObj.put("Data", TeldUtil.Encrypt(jsonParam, aesKey, aesIV));
        String dateStr = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_DEFAULT);
        paramObj.put("TimeStamp", dateStr);
        paramObj.put("Seq", getSeq(dateStr));

        String signMsg = paramObj.getString("OperatorID") + paramObj.getString("Data")
                + paramObj.getString("TimeStamp") + paramObj.getString("Seq");

        paramObj.put("Sig", TeldUtil.getHmacMd5Str(signKey, signMsg));

        return paramObj.toString();
    }

    /**
     * 生成加密报文
     *
     * @param jsonParam
     * @param aesKey
     * @param aesIV
     * @param signKey
     * @param OPERATOR_ID
     * @return
     * @throws Exception
     */
    public static String generateReturnParam(String jsonParam, String aesKey, String aesIV, String signKey, String OPERATOR_ID)
            throws Exception {
        System.out.println("generateSecurityParam=" + jsonParam);
        JSONObject paramObj = new JSONObject();
        paramObj.put("Ret", 0);
        paramObj.put("Data", TeldUtil.Encrypt(jsonParam, aesKey, aesIV));
        paramObj.put("Msg", "成功");

        String signMsg = paramObj.getString("Ret") +paramObj.getString("Msg") + paramObj.getString("Data");

        paramObj.put("Sig", TeldUtil.getHmacMd5Str(signKey, signMsg));

        return paramObj.toString();
    }

    public static String getSeq(String timeStamp) {

        if (map.containsKey(timeStamp)) {
            Integer count = Integer.parseInt(map.get(timeStamp)) + 1;
            switch (count.toString().length()) {
                case 1:
                    map.put(timeStamp, "000" + count);
                    break;
                case 2:
                    map.put(timeStamp, "00" + count);
                    break;
                case 3:
                    map.put(timeStamp, "0" + count);
                    break;
                case 4:
                    map.put(timeStamp, "" + count);
                    break;
                default:
                    map.put(timeStamp, "" + count);
                    break;
            }
            return map.get(timeStamp);
        } else {
            map.put(timeStamp, "0001");
            return "0001";
        }
    }
    public static void main(String[] args) throws Exception {
        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
         * 此处使用AES-128-CBC加密模式，key需要为16位。
         */
        String cKey = "1234567890abcdef";
        // 需要加密的字串
        String cSrc = "1234567890'[]:";
        String ivStr = "12345789";
        cSrc = "{\"province\": \"山东\",\"city\": \"青岛\",\"region\": \"\",\"type\": \"\", \"opState\": \"\",\"pageNo\": \"1\",\"pageSize\": \"2\"}";
        // cSrc="示例：{\"total\":1,\"stationStatusInfo\":{\"operationID\":\"123456789\",\"stationID\":\"111111111111111\",\"connectorStatusInfos\":{\"connectorID\":1,\"equipmentID\":\"10000000000000000000001\",\"status\":4,\"currentA\":0,\"currentB\":0,\"currentC\":0,\"voltageA\":0,\"voltageB\":0,\"voltageC\":0,\"soc\":10,}";
        System.out.println(cSrc);
        // 加密
        long lStart = System.currentTimeMillis();
        String enString = Encrypt(cSrc, cKey, ivStr);
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
        // 解密
        lStart = System.currentTimeMillis();
        // AESUtil.Decrypt("DHVWF+8xRIfU7nUCNQdLaGF15VaMZWtNcwaqeumUPe/ok9zgSkR0pbOJUmYYQs7ZFMN7GhLB1ywEN3kb1gH4z+Mc2Z4rQe8Xa42LrmkDRvwwosmVMuR+mbLFCG+Xf5unkRO6JJx1PiTAxAB6oyWqUmbOKskK81LqpWBU5fKnBZwXo3jv2hnKItwCODYw+B+Pg+0IzZ5ye5cKcwz99NO5//H2gU0scZhn+rl8Jcktbm42TVklnxdzG/aw200H2z9ugpB1q2X0sGAi55SQH3DbLpWb5oQE5vy0As7lje4e+4dE8vbLIR0dMw8/lA9cBPYRO2WOkH6SFwFUyi+IishP8j+mzEcfoyAOIUSh5G/5VYqlYu1zlVUsYCHWu7MTE1Gr55SicHZQxl5KHgmgFBw8OQl8DerA++D8vswR8eiRNcXR2MQmNXYarCmZ7Kuc6mRSbrSk2cImOZAywVIo6MpBSu/u0BINysS3S7Ni1LB6zqAu3Ly0yNbbxzz+ZpHjmAM+MMsx4/K6LtG1rhiW8iE3bbGOLJqu9njLFVLQtKXrVsUnF4b1FWuIADG3FBCXqcdyTTTj5vNwI2xxFm/zq5lvJUKUlcFPvYSwBQFsjKHZnl8=",
        // cKey);
        String DeString = Decrypt(enString, cKey, ivStr);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");
    }
}
