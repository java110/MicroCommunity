package com.java110.acct.payment.adapt.easypay;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.adapt.easypay.utils.RsaUtil;
import com.java110.utils.util.StringUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *  基础支付请求类,提供公共参数和方法信息
 *
 * @date: 2021/10/22 11:31
 * @author: pandans
 */
public class BasePay extends Object{
    /**
     * 收银台请求地址
     */
    public static final String CASHIER_URL = "https://mtest.eycard.cn";
//    public static final String CASHIER_URL = "http://10.10.14.128:8805";
    /**
     * 微收单优化业务请求地址
     */
    public static final String BASE_URL = "https://t-wapi.bhecard.com:8443";
    /**
     * 分账，退货地址
     */
    public static final String BASE_URL_LEDGER = "https://t-wapi.bhecard.com:6111";

    /**
     * 默认的接口签名方式
     */
    public static final String SIGN_TYPE_RSA256 = "RSA2";
    public static final String SIGN_TYPE_RSA = "RSA";
//

    /**
     * 接口返回成功代码
     */
    public static final String RET_CODE_SUCCESS = "000000";



    /**
     * 排序拼接需要签名的数据， ！！参数值不为空且trim不为空。
     *
     * @param dataObj 数据对象 不能都为空
     * @return 签名排序字符串
     */
    public static String getReqStr(Object dataObj) {

        JSONObject data = (JSONObject) JSON.toJSON(dataObj);

//        data = JSONObject.parseObject("{\"tradeAmt\":\"9\",\"orgBackUrl\":\"https://pay-test.jian9999.cn/gateway/notify/EASYPAYALIPAY\",\"splitSettleFlag\":\"1\",\"payerId\":\"123123123\",\"orderInfo\":\"购买苹果手机一个\",\"tradeCode\":\"WUJS1\"}");

        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            // 参数值为空，则不参与签名
            if (data.get(k) != null && data.get(k).toString().trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
            }
        }
        return sb.substring(0, sb.toString().lastIndexOf("&"));
    }

    /**
     * 验证易生返回的报文数据. !!!使用data字段的内容验签
     *
     * @param response 易生返回的数据
     * @return 验签是否成功
     */
    public static boolean checkSign(String response,String EASYPAY_PUBLIC_KEY) {
        JSONObject json = JSON.parseObject(response);
        // 云易收sysRetCode 和 微收单 sysRetcode
        if ("000000".equals(json.getString("sysRetcode")) || "000000".equals(json.getString("sysRetCode"))) {
            String sign = json.getString("sign");
            if (StringUtil.isEmpty(sign)) {
                System.out.println("=====> 签名失败，该报文未返回签名数据");
                return false;
            }
            JSONObject jsonData = json.getJSONObject("data");
            boolean ret = RsaUtil.verifyBySHA256WithRSA(getReqStr(jsonData), sign, EASYPAY_PUBLIC_KEY);

            if (ret) {
                System.out.println("=====> 验签成功，接口数据安全");
                return true;
            } else {
                System.out.println("=====> 验签失败，请注意报文安全");
                return false;
            }
        } else {
            System.out.println("=====> 非00状态码，不需要验签");

            return false;
        }
    }
    public static boolean checkBizSign(String response,String EASYPAY_PUBLIC_KEY) {
        JSONObject json = JSON.parseObject(response);
        // 云易收sysRetCode 和 微收单 sysRetcode
        if ("000000".equals(json.getString("sysRetcode")) || "000000".equals(json.getString("sysRetCode"))) {
            String sign = json.getString("sign");
            if (StringUtil.isEmpty(sign)) {
                System.out.println("=====> 签名失败，该报文未返回签名数据");
                return false;
            }
            JSONObject jsonData = json.getJSONObject("bizData");
            boolean ret = RsaUtil.verifyBySHA256WithRSA(getReqStr(jsonData), sign, EASYPAY_PUBLIC_KEY);

            if (ret) {
                System.out.println("=====> 验签成功，接口数据安全");
                return true;
            } else {
                System.out.println("=====> 验签失败，请注意报文安全");
                return false;
            }
        } else {
            System.out.println("=====> 非00状态码，不需要验签");

            return false;
        }
    }

    /**
     * 使用商户的私钥进行加签
     *
     * @param data: 签名数据，注意这里仅仅是文档中data字段的内容
     * @return java.lang.String 签名
     */
    public static String sign(Object data, String signKey) {
        String reqStr = getReqStr(data);
        String res = RsaUtil.signBySHA256WithRSA(reqStr, signKey);
        return res;
    }

    public static String signStr(String data, String signKey) {
        return sign(JSON.parseObject(data), signKey);
    }

    /**
     * 计算sign
     * @param request 请求内容不包含sign
     * @param signKey 签名key
     */
    public static String calSign(Map<String, Object> request, String signKey) {
        Set<String> keySet = request.keySet();
        TreeSet<String> treeSet = new TreeSet<>(keySet);
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : treeSet) {
            if(!"MAC".equals(key) && !StringUtil.isEmpty(request.get(key).toString())) {
                stringBuilder.append(key).append("=").append(request.get(key)).append("&");
            }
        }
        stringBuilder.append("key=").append(signKey);
        String str = stringBuilder.toString();
        System.out.println("待签名字段：：====>" + str);
        String md5 = calMD5(str);
        return md5;
    }

    /**
     * MD5 算法
     * @param data 待计算源数据
     * @return md5值
     */
    public static String calMD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            return toHexString(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final char[] HEX_CHAR = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    public static String toHexString(byte[] bytes) {
        char[] buf =  new char[bytes.length * 2];
        int index = 0;
        for(byte b: bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

}