package com.java110.front.controller.app;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @program: openPlatform
 * @description: 接收并处理支付结果通知的demo
 * @author: Mr.YS
 * @CreatDate: 2019/12/25/025 16:42
 */
public class NotifyDemo {
    /**
     * md5秘钥
     */
    static final String md5key = "JkENP4taKmyH2aBsxXZbnpJDGZ7pBhasCKcYxpt7xyNP4QXS";


    /**
     * 支付通知模块，用于：在支付成功后 接受银商平台发送的支付结果通知
     */
//    @RequestMapping(value = "/notify.do", method = RequestMethod.POST)
    public static void main(String[] args) throws Exception {

        /*接收参数*/
        String p = "{msgType=wx.notify, payTime=2021-09-06 14:45:10, buyerCashPayAmt=10, connectSys=UNIONPAY, sign=93A2A241A84F822B37E13895A1D6FD8CB4CCABEA095A60EFC9646EC4594F5114, merName=青海德坤电力有限公司, mid=898630149000110, invoiceAmount=10, settleDate=2021-09-06, billFunds=现金:10, buyerId=otdJ_uCsgFQi-XigMpadM9gB4h0w, mchntUuid=2d9081bd76d235d20176da1bf4f62bc9, tid=CV5EW7IM, instMid=YUEDANDEFAULT, receiptAmount=10, couponAmount=0, targetOrderId=4200001180202109067432700837, signType=SHA256, billFundsDesc=现金支付0.10元。, subBuyerId=oBFo-5-xs50SKaC5hjYf2Ux_Ww2g, orderDesc=青海德坤电力有限公司, seqId=23389562168N, merOrderId=11WP102021090602190026, targetSys=WXPay, bankInfo=OTHERS, Ru=NcFv, totalAmount=10, wId=hFXywDBfLkpKik7ZLPlAsRUQ4qORS1n8, createTime=2021-09-06 14:45:03, buyerPayAmount=10, notifyId=d16d35c6-a53d-4ee4-89d1-f08fd8c7b462, subInst=103800, status=TRADE_SUCCESS}";
        Map<String, String> params =mapStringToMap(p);
        System.out.println("params:" + params);
        String sign = params.get("sign");
        System.out.println(sign);


        /*验签*/
        //对通知内容生成sign
        String strSign = makeSign(md5key, params);
        //System.out.println("strSign="+strSign);
        //判断签名是否相等
        if (sign.equals(strSign)) {
            // 收到通知后记得返回SUCCESS
            System.out.println("验签通过");
        } else {
            System.out.println("验签未通过");
        }
    }

    public static Map<String,String> mapStringToMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key=string.split("=")[0];
            String value=string.split("=")[1];
            map.put(key.trim(), value.trim());
        }
        return map;
    }


    // 获取HttpServletRequest里面的参数
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            if (values.length > 0) {
                params2.put(key, request.getParameter(key));
            }
        }
        return params2;
    }


    public static String makeSign(String md5Key, Map<String, String> params) {

        String preStr = buildSignString(params); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String text = preStr + md5Key;
        System.out.println("待签名字符串：" + text);
        return DigestUtils.sha256Hex(getContentBytes(text)).toUpperCase();
    }

    // 构建签名字符串
    public static String buildSignString(Map<String, String> params) {

        // params.put("Zm","test_test");

        if (params == null || params.size() == 0) {
            return "";
        }

        List<String> keys = new ArrayList<>(params.size());

        for (String key : params.keySet()) {
            if ("sign".equals(key))
                continue;
            if ("wId".equals(key))
                continue;
            if (StringUtils.isEmpty(params.get(key)))
                continue;
            keys.add(key);
        }
        //System.out.println(listToString(keys));

        Collections.sort(keys);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                buf.append(key + "=" + value);
            } else {
                buf.append(key + "=" + value + "&");
            }
        }

        return buf.toString();
    }

    // 根据编码类型获得签名内容byte[]
    public static byte[] getContentBytes(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误");
        }
    }
}
