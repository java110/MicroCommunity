package com.java110.utils.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Convert {

    private void Base64Convert() {

    }

    /**
     * 流转换为字符串
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String ioToBase64(InputStream in) throws IOException {
        String strBase64 = null;
        try {
            // in.available()返回文件的字节长度
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return strBase64;
    }

    /**
     * 流转换为字符串
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String byteToBase64(byte[] bytes)  {
        String strBase64 = null;
            // in.available()返回文件的字节长度
            strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
        return strBase64;
    }


   

    /**
     * 将base64 转为字节
     *
     * @param strBase64
     * @return
     * @throws IOException
     */
    public static byte[] base64ToByte(String strBase64) throws IOException {
        // 解码，然后将字节转换为文件
        byte[] bytes = new BASE64Decoder().decodeBuffer(strBase64);   //将字符串转换为byte数组
        return bytes;
    }
}
