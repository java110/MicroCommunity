package com.java110.job.adapt.returnMoney.bbg.lib;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 国密加密算法
 */
public class GmUtil {
	private static final String DEFAULT_CHARSET = "UTF-8";
	public static final String ALGORITHM_NAME = "SM4";
	// 加密算法/分组加密模式/分组填充方式
	// PKCS5Padding-以8个字节为一组进行分组加密
	// 定义分组加密模式使用：PKCS5Padding
	public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
	// 128-32位16进制；256-64位16进制
	public static final int DEFAULT_KEY_SIZE = 128;
	// 椭圆曲线ECParameters ASN.1 结构
	private static X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
	// 椭圆曲线公钥或私钥的基本域参数。
	private static ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 将Base64转码的公钥串，转化为公钥对象
	 *
	 */
	public static PublicKey createPublicKey(String publicKey) {
		PublicKey publickey = null;
		try {
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64Util.decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
			publickey = keyFactory.generatePublic(publicKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publickey;
	}

	/**
	 * 将Base64转码的私钥串，转化为私钥对象
	 *
	 */
	public static PrivateKey createPrivateKey(String privateKey) {
		PrivateKey publickey = null;
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
			publickey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publickey;
	}

	/**
	 * 根据publicKey对原始数据data，使用SM2加密
	 */
	public static byte[] encrypt(byte[] data, PublicKey publicKey) {
		ECPublicKeyParameters localECPublicKeyParameters = null;

		if (publicKey instanceof BCECPublicKey) {
			BCECPublicKey localECPublicKey = (BCECPublicKey) publicKey;
			ECParameterSpec localECParameterSpec = localECPublicKey.getParameters();
			ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN());
			localECPublicKeyParameters = new ECPublicKeyParameters(localECPublicKey.getQ(), localECDomainParameters);
		}
		SM2Engine localSM2Engine = new SM2Engine();
		localSM2Engine.init(true, new ParametersWithRandom(localECPublicKeyParameters, new SecureRandom()));
		byte[] arrayOfByte2;
		try {
			arrayOfByte2 = localSM2Engine.processBlock(data, 0, data.length);
			return arrayOfByte2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据privateKey对加密数据encodedata，使用SM2解密
	 */
	public static byte[] decrypt(byte[] encodedata, PrivateKey privateKey) {
		SM2Engine localSM2Engine = new SM2Engine();
		BCECPrivateKey sm2PriK = (BCECPrivateKey) privateKey;
		ECParameterSpec localECParameterSpec = sm2PriK.getParameters();
		ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN());
		ECPrivateKeyParameters localECPrivateKeyParameters = new ECPrivateKeyParameters(sm2PriK.getD(), localECDomainParameters);
		localSM2Engine.init(false, localECPrivateKeyParameters);
		try {
			byte[] arrayOfByte3 = localSM2Engine.processBlock(encodedata, 0, encodedata.length);
			return arrayOfByte3;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 私钥签名
	 */
	public static byte[] signByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
		Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
		sig.initSign(privateKey);
		sig.update(data);
		return sig.sign();
	}

	/**
	 * 公钥验签
	 */
	public static boolean verifyByPublicKey(byte[] data, PublicKey publicKey, byte[] signature) throws Exception {
		Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
		sig.initVerify(publicKey);
		sig.update(data);
		return sig.verify(signature);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @Description 公钥字符串转换为 BCECPublicKey 公钥对象
	 * @param pubKeyHex
	 *            64字节十六进制公钥字符串(如果公钥字符串为65字节首个字节为0x04：表示该公钥为非压缩格式，操作时需要删除)
	 * @return BCECPublicKey SM2公钥对象
	 */
	private static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
		// 截取64字节有效的SM2公钥（如果公钥首个字节为0x04）
		if (pubKeyHex.length() > 128) {
			pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
		}
		// 将公钥拆分为x,y分量（各32字节）
		String stringX = pubKeyHex.substring(0, 64);
		String stringY = pubKeyHex.substring(stringX.length());
		// 将公钥x、y分量转换为BigInteger类型
		BigInteger x = new BigInteger(stringX, 16);
		BigInteger y = new BigInteger(stringY, 16);
		// 通过公钥x、y分量创建椭圆曲线公钥规范
		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
		// 通过椭圆曲线公钥规范，创建出椭圆曲线公钥对象（可用于SM2加密及验签）
		return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
	}

	/**
	 * @Description 私钥字符串转换为 BCECPrivateKey 私钥对象
	 * @param privateKeyHex
	 *            32字节十六进制私钥字符串
	 * @return BCECPrivateKey SM2私钥对象
	 */
	private static BCECPrivateKey getBCECPrivateKeyByPrivateKeyHex(String privateKeyHex) {
		// 将十六进制私钥字符串转换为BigInteger对象
		BigInteger d = new BigInteger(privateKeyHex, 16);
		// 通过私钥和私钥域参数集创建椭圆曲线私钥规范
		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecDomainParameters);
		// 通过椭圆曲线私钥规范，创建出椭圆曲线私钥对象（可用于SM2解密和签名）
		return new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
	}

	/**
	 * @Description 公钥加密
	 */
	public static String encryptSm2(String data, String publicKeyHex) {
		BCECPublicKey publicKey = getECPublicKeyByPublicKeyHex(publicKeyHex);
		// 加密模式
		SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
		// 通过公钥对象获取公钥的基本域参数。
		ECParameterSpec ecParameterSpec = publicKey.getParameters();
		ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
		// 通过公钥值和公钥基本参数创建公钥参数对象
		ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
		// 根据加密模式实例化SM2公钥加密引擎
		SM2Engine sm2Engine = new SM2Engine(mode);
		// 初始化加密引擎
		sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
		byte[] arrayOfBytes = null;
		try {
			// 将明文字符串转换为指定编码的字节串
			byte[] in = Hex.decode(data);
			// 通过加密引擎对字节数串行加密
			arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将加密后的字节串转换为十六进制字符串
		return Hex.toHexString(arrayOfBytes).toUpperCase();
	}

	/**
	 * @Description 私钥解密
	 */
	public static String decryptSm2(String cipherData, String privateKeyHex) {
		BCECPrivateKey privateKey = getBCECPrivateKeyByPrivateKeyHex(privateKeyHex);
		// 解密模式
		SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
		// 将十六进制字符串密文转换为字节数组（需要与加密一致，加密是：加密后的字节数组转换为了十六进制字符串）
		byte[] cipherDataByte = Hex.decode(cipherData);
		// 通过私钥对象获取私钥的基本域参数。
		ECParameterSpec ecParameterSpec = privateKey.getParameters();
		ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());
		// 通过私钥值和私钥钥基本参数创建私钥参数对象
		ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(), ecDomainParameters);
		// 通过解密模式创建解密引擎并初始化
		SM2Engine sm2Engine = new SM2Engine(mode);
		sm2Engine.init(false, ecPrivateKeyParameters);
		String result = null;
		try {
			// 通过解密引擎对密文字节串进行解密
			byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
			result = new String(arrayOfBytes, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 自动生成sm4密钥
	 *
	 */
	public static String generateSm4Key() throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
		kg.init(DEFAULT_KEY_SIZE, new SecureRandom());
		byte[] sm4Key = kg.generateKey().getEncoded();
		return new String(Hex.encode(sm4Key)).toUpperCase();
	}

	/**
	 * 生成ECB暗号
	 *
	 */
	private static Cipher generateSm4Cipher(String algorithmName, int mode, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
		Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
		cipher.init(mode, sm4Key);
		return cipher;
	}

	/**
	 * sm4加密
	 *
	 */
	public static String encryptSm4(String data, String hexKey) {
		try {
			// 16进制字符串-->byte[]
			byte[] keyData = Hex.decode(hexKey);
			// String-->byte[]
			byte[] srcData = data.getBytes(DEFAULT_CHARSET);
			// 加密后的数组
			Cipher cipher = generateSm4Cipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, keyData);
			byte[] cipherArray = cipher.doFinal(srcData);
			// byte[]-->hexString
			return Hex.toHexString(cipherArray).toUpperCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * sm4解密
	 *
	 */
	public static String decryptSm4(String cipherText, String hexKey) {
		// 用于接收解密后的字符串
		String decryptStr = null;
		// hexString-->byte[]
		byte[] keyData = Hex.decode(hexKey);
		// hexString-->byte[]
		byte[] cipherData = Hex.decode(cipherText);
		try {
			// 解密
			Cipher cipher = generateSm4Cipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, keyData);
			byte[] srcData = cipher.doFinal(cipherData);
			decryptStr = new String(srcData, DEFAULT_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptStr;
	}

}
