package com.java110.common.smartMeter.factory.tqdianbiao;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * 拓强智能电表 工具类
 */
public class TdDianBiaoUtil {

    // 请求接口
    public static String requestAsync(String url, String request_content,String notifyUrl){
        // 时间戳
        String timestamp = String.valueOf(new Date().getTime()/1000);

        // 用于签名的内容
        Map<String, String> data = new HashMap<>();
        data.put("timestamp", timestamp);
        data.put("auth_code", MappingCache.getValue(MappingConstant.TDDIANBIAO_DOMAIN,"auth_code"));
        data.put("request_content", request_content);
        data.put("notify_url", notifyUrl);

        // 获取签名
        String sign = getSign(data);

        data.put("sign", sign);

        try {
            return sendHttpRequest(url, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String sendHttpRequest(String url, Map<String, String> bodyMap) throws Exception {
        System.out.println("请求地址：" + url);
        System.out.println("发送参数：" + bodyMap.toString());
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<>();

        for(String key : bodyMap.keySet()) {
            nvps.add(new BasicNameValuePair(key,bodyMap.get(key)));
        }
        postRequest.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));


        int retry = 3;
        HttpResponse execute = null;
        while(retry-- > 0) {
            try {
                execute = client.execute(postRequest);
                break;
            } catch (Exception e) {
                Thread.sleep(5000);
            }
        }
        if(execute == null) {
            throw new Exception("接口请求失败");
        }
        String resp = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("接口返回：" + resp);
        return resp;
    }

    // 生成签名字符串
    private static String getSign(Map<String, String> data)
    {
        // 获取关键字列表
        List<String> keys = new ArrayList<>(data.keySet());
        // 关键字列表排序
        keys.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        for (String key : keys)
        {
            // 取各个字段内容拼接字符串
            sb.append(data.get(key));
        }
        // 加上双方约定随机字符串
        String txt = sb.toString() + MappingCache.getValue(MappingConstant.TDDIANBIAO_DOMAIN,"nonce");

        // 计算哈希值
        return getMD5(txt);
    }

    // md5加密
    private static String getMD5(String password) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] byteArray = password.getBytes(StandardCharsets.UTF_8);

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static boolean  checkSign(String response_content, String timestamp, String sign) {
        // 随机字符串 后台获取
        String nonce = MappingCache.getValue(MappingConstant.TDDIANBIAO_DOMAIN,"nonce");
        String buf = response_content + timestamp + nonce;
        String encode = getMD5(buf);
        return encode.equals(sign);
    }
}
