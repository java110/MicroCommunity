package com.java110.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;

import java.util.Date;

/**
 * 接口返回模板类
 *
 * {
     "orders": {
         "TransactionId": "100000000020180409224736000001",
         "ResponseTime": "20180409224736",
         "sign": "这个服务是否要求MD5签名",
         "response": {//这个是centerOrder 返回的状态结果
         "code": "1999",
         "message": "具体值"
         }
     },
     "business":[{//这个是相应的业务系统返回的结果，（受理为空，查询时不为空）
         "response": {
         "code": "1999",
         "message": "具体值"
         }
     //相应内容
     }]
 }
 * Created by wuxw on 2018/4/13.
 */
public class ResponseTemplateUtil {

    /**
     * 创建 通用返回结果模板
     * @param transactionId 交易流水
     * @param sign 签名
     * @param code 错误
     * @param message 错误信息
     * @param business 业务信息
     * @return
     */
    public static String createCommonResponseJson(String transactionId,
                                                  String sign,
                                                  String code,
                                                  String message,
                                                  JSONArray business){

        JSONObject responseInfo = JSONObject.parseObject("{\"orders\":{\"response\":{}}}");
        JSONObject orderInfo = responseInfo.getJSONObject("orders");
        orderInfo.put("TransactionId",transactionId);
        orderInfo.put("ResponseTime",DateUtil.getDefaultFormateTimeString(new Date()));
        orderInfo.put("sign",sign);
        JSONObject orderResponseInfo = orderInfo.getJSONObject("response");
        orderResponseInfo.put("code",code);
        orderResponseInfo.put("message",message);
        if (business != null && business.size() > 0){
            responseInfo.put("business",business);
        }
        return responseInfo.toJSONString();
    }

    /**
     * 返回模板 只有Order信息
     * @param transactionId
     * @param sign
     * @param code
     * @param message
     * @return
     */
    public static String createOrderResponseJson(String transactionId, String sign,String code,String message){
        return createCommonResponseJson(transactionId,sign,code,message,null);
    }
}
