package com.java110.acct.payment.adapt.bbgpay.lib;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSONObject;

/**
 * aes 加解密相关工具类
 * 
 * @author bbw
 *
 */
public class AesEncrypt {
	public final static String ivParameter = "0000000000000000";
	private static final String CHARSET_NAME_UTF8 = "UTF-8";
	
    static {
        Security.addProvider(new BouncyCastleProvider());
        Security.setProperty("crypto.policy", "unlimited");
    }

	public static String encrypt(String sSrc, String encodingFormat, String sKey) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
		return Base64Util.encode(encrypted);
	}

	public static byte[] encryptByte(String sSrc, String encodingFormat, String sKey) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
		return encrypted;
	}

	public static byte[] encryptByte(String sSrc, String encodingFormat, byte[] raw) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
		return encrypted;
	}

	public static String decrypt(String sSrc, String encodingFormat, String sKey) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64Util.decode(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, encodingFormat);
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static byte[] decryptByte(byte[] bt, String sKey) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		try {
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(bt);
			return original;
		} catch (Exception ex) {
			return null;
		}
	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	// 法院一案一户加密
	public static String ocoaEncrypt(String content, String aesKey, String aesIV) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
		SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(CHARSET_NAME_UTF8), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(aesIV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
		byte[] result = cipher.doFinal(content.getBytes(CHARSET_NAME_UTF8));
		return URLEncoder.encode(Base64Util.encode(result), "UTF-8");
	}

	// 法院一案一户解密
	public static String ocoaDecrypt(String content, String aesKey, String aesIV) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(CHARSET_NAME_UTF8), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(aesIV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
			return new String(cipher.doFinal(Base64Util.decode(URLDecoder.decode(content, "UTF-8"))), CHARSET_NAME_UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	  * 异或运算
	  * 
	  */
	public static String strEncAndDec(String src) {
		// 字符串转成字符数组
		char[] c = src.toCharArray();
		// 循环给字符数组加密
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) (c[i] ^ 1);
		}
		return new String(c);
	}

    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ZH", "0003999");
        jsonObject.put("AH", "");
        String content = JSONObject.toJSONString(jsonObject);
        String s1 = ocoaEncrypt(content, "2021YHSYSHYAKXTK", "2021YHSYSHYAKXTV");
        System.out.println("密文:" + s1);
        String result = ocoaDecrypt(s1, "2021YHSYSHYAKXTK", "2021YHSYSHYAKXTV");
        System.out.println("解密:" + result);
        System.out.println(14584308/16415104f);
        
        String authCode = "990000003458LBU3133170411108125";
        System.out.println("付款码:" + authCode);
        String mchtNo = authCode.substring(12);
        System.out.println("第13位开始截取:" + mchtNo);
        System.out.println("企业商户号:" + strEncAndDec(mchtNo));
    }
    
}
