package com.java110.acct.payment.adapt.easypay.utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * Base64封装类，使用java8的内置Base64
 *
 * @Date: 2021/10/14 20:07
 * @Author: pandans
 */
public class Base64 {
    public static final String UTF_8 = "UTF-8";
    public static java.util.Base64.Encoder encoder;
    public static java.util.Base64.Encoder urlEncoder;
    public static java.util.Base64.Decoder decoder;
    public static java.util.Base64.Decoder urlDecoder;

    static {
        encoder = java.util.Base64.getEncoder();
        urlEncoder = java.util.Base64.getUrlEncoder();
        decoder = java.util.Base64.getDecoder();
        urlDecoder = java.util.Base64.getUrlDecoder();
    }


    public static byte[] encode(byte[] bytes) {
        return encoder.encode(bytes);
    }

    public static String encode(String str) {
        byte[] encode = encode(str.getBytes());
        return new String(encode, StandardCharsets.UTF_8);
    }

    public static String encode2String(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    public static byte[] encode2Byte(String string) {
        return encode(string.getBytes());
    }

    //urlEncoder
    public static byte[] urlEncode(byte[] bytes) {
        return urlEncoder.encode(bytes);
    }

    public static String urlEncode(String string) {
        byte[] encode = urlEncode(string.getBytes());
        try {
            return new String(encode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlEncode2String(byte[] bytes) {
        return urlEncoder.encodeToString(bytes);
    }

    public static byte[] urlEncode2Byte(String string) {
        return urlEncode(string.getBytes());
    }

    //decode
    public static byte[] decode(byte[] bytes) {
        return decoder.decode(bytes);
    }

    public static byte[] decode2Byte(String string) {
        return decoder.decode(string.getBytes());
    }

    public static String decode2String(byte[] bytes) {
        try {
            return new String(decoder.decode(bytes), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String string) {
        byte[] decode = decode(string.getBytes());
        try {
            return new String(decode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //urlDecode
    public static byte[] urlDecode(byte[] bytes) {
        return urlDecoder.decode(bytes);
    }

    public static byte[] urlDecode2Byte(String string) {
        return urlDecode(string.getBytes());
    }

    public static String urlDecode2String(byte[] bytes) {
        try {
            return new String(urlDecode(bytes), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlDecode(String string) {
        byte[] decode = urlDecode(string.getBytes());
        try {
            return new String(decode, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
