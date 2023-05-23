package com.java110.job.adapt.returnMoney.bbg.lib;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

/**
 * 验证证书公共类
 * 
 */
public class CAUtil {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String KEY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	/**
	 * 判断字符串是否为null或空 true为空
	 */
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	// 获取私钥
	public static PrivateKey getPrivateKey(InputStream is, String privateKeyPwd) throws Exception {
		KeyStore ks;
		try {
			ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = null;
			if (isNullOrEmpty(privateKeyPwd)) {
				privateKeyPwd = null;
			} else {
				nPassword = privateKeyPwd.toCharArray();
			}
			ks.load(is, nPassword);
			is.close();
			Enumeration<?> enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements()) {
				keyAlias = (String) enumas.nextElement();
			}
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			return prikey;
		} catch (KeyStoreException e) {
			throw new Exception("获取KeyStore失败");
		} catch (FileNotFoundException e) {
			throw new Exception("无效的私钥地址");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("读取私钥失败");
		} catch (CertificateException e) {
			throw new Exception("加载证书失败");
		} catch (IOException e) {
			throw new Exception("读取证书失败");
		} catch (UnrecoverableKeyException e) {
			throw new Exception("获取私钥失败");
		}
	}

	// 获取公钥
	public static PublicKey getPublicKey(String publicKeyAddr) throws Exception {
		try {
			CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
			FileInputStream bais = new FileInputStream(publicKeyAddr);
			X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(bais);
			bais.close();
			PublicKey pk = Cert.getPublicKey();
			return pk;
		} catch (CertificateException e) {
			throw new Exception("获取公钥失败");
		}
	}

	/**
	 * RSA签名
	 * 
	 * @param localPrivKey
	 *            私钥
	 * @param plaintext
	 *            需要签名的信息
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] signRSA(byte[] plainBytes, boolean useBase64Code, PrivateKey privKey) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privKey);
		signature.update(plainBytes);
		// 如果是Base64编码的话，需要对签名后的数组以Base64编码
		if (useBase64Code) {
			return Base64.encodeBase64(signature.sign());
		} else {
			return signature.sign();
		}
	}

	/**
	 * 验签操作
	 * 
	 * @param peerPubKey
	 *            公钥
	 * @param plainBytes
	 *            需要验签的信息
	 * @param signBytes
	 *            签名信息
	 * @return boolean
	 */
	public static boolean verifyRSA(byte[] plainBytes, byte[] signBytes, boolean useBase64Code, PublicKey pubKey) throws Exception {
		boolean isValid = false;
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(plainBytes);
		// 如果是Base64编码的话，需要对验签的数组以Base64解码
		if (useBase64Code) {
			isValid = signature.verify(Base64.decodeBase64(signBytes));
		} else {
			isValid = signature.verify(signBytes);
		}
		return isValid;
	}
	
	/**
	 * SHA256withRSA签名(RSA2)
	 * 
	 */
	public static String rsa256Sign(String content, String charset, String privateKey) throws Exception {
		if (isNullOrEmpty(charset)) {
			charset = DEFAULT_CHARSET;
		}
		if (isNullOrEmpty(privateKey)) {
			return null;
		}
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		byte[] encodedKey = StreamUtil.readText(new ByteArrayInputStream(privateKey.getBytes())).getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		if (isNullOrEmpty(charset)) {
			signature.update(content.getBytes());
		} else {
			signature.update(content.getBytes(charset));
		}
		byte[] signed = signature.sign();
		return new String(Base64.encodeBase64(signed));
	}

	/**
	 * SHA256withRSA验签(RSA2)
	 * 
	 */
	public static boolean rsa256Verify(byte[] content, String sign, String publicKey) throws Exception {
		if (isNullOrEmpty(publicKey)) {
			return false;
		}
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		StringWriter writer = new StringWriter();
		StreamUtil.io(new InputStreamReader(new ByteArrayInputStream(publicKey.getBytes())), writer);
		byte[] encodedKey = writer.toString().getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(content);
		return signature.verify(Base64.decodeBase64(sign.getBytes()));
	}
}