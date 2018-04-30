package com.java110.common.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.OrderTypeCdConstant;
import com.java110.common.util.DateUtil;
import com.java110.common.util.SequenceUtil;
import com.java110.common.util.StringUtil;
import com.java110.entity.center.DataFlow;

import java.util.Date;
import java.util.Map;

/**
 * 请求信息封装工厂类
 * Created by wuxw on 2018/4/28.
 */
public class DataTransactionFactory {


    /**
     * 创建 通用返回结果模板
     * @param transactionId 交易流水
     * @param code 错误
     * @param message 错误信息
     * @param business 业务信息
     * @return
     */
    public static JSONObject createCommonResponseJson(String transactionId,
                                                      String code,
                                                      String message,
                                                      JSONArray business){

        JSONObject responseInfo = JSONObject.parseObject("{\"orders\":{\"response\":{}}}");
        JSONObject orderInfo = responseInfo.getJSONObject("orders");
        orderInfo.put("transactionId",transactionId);
        orderInfo.put("responseTime",DateUtil.getDefaultFormateTimeString(new Date()));
        JSONObject orderResponseInfo = orderInfo.getJSONObject("response");
        orderResponseInfo.put("code",code);
        orderResponseInfo.put("message",message);
        if (business != null && business.size() > 0){
            responseInfo.put("business",business);
        }
        return responseInfo;
    }

    /**
     * 返回模板 只有Order信息
     * @param transactionId
     * @param code
     * @param message
     * @return
     */
    public static JSONObject createOrderResponseJson(String transactionId,String code,String message){
        return createCommonResponseJson(transactionId,code,message,null);
    }

    /**
     * 组装返回报文
     * @param dataFlow 数据流
     * @return
     */
    public static JSONObject createCommonResponseJson(DataFlow dataFlow){
        return dataFlow.getResponseBusinessJson();
    }

    /**
     * 业务系统返回报文模板
     * @param code
     * @param message
     * @param
     * @return
     */
    public static JSONObject createBusinessResponseJson(String code, String message){
        return   createBusinessResponseJson(code, message,null);
    }
    /**
     * 业务系统返回报文模板
     * @param code
     * @param message
     * @param info
     * @return
     */
    public static JSONObject createBusinessResponseJson(String code, String message, JSONObject info){
        JSONObject responseMessage = JSONObject.parseObject("{\"response\":{}}");

        JSONObject response = responseMessage.getJSONObject("response");

        response.put("code",code);
        response.put("message",message);
        if(info != null) {
            responseMessage.putAll(info);
        }

        return responseMessage;
    }

    /**
     * 创建查询 请求 centerService json 报文
     * @param appId
     * @param userId
     * @param sign
     * @param business
     * @return
     */
    public static String createQueryOneCenterServiceRequestJson(String appId,String userId,String sign,
                                                             JSONObject business){
        JSONArray businesses = new JSONArray();
        businesses.add(business);
        return createCenterServiceRequestJson(appId,userId, OrderTypeCdConstant.ORDER_TYPE_CD_QUERY,sign,"",null,businesses);
    }


    /**
     * 创建查询 请求 centerService json 报文
     * @param appId
     * @param userId
     * @param sign
     * @param businesses
     * @return
     */
    public static String createQueryCenterServiceRequestJson(String appId,String userId,String sign,
                                                        JSONArray businesses){
        return createCenterServiceRequestJson(appId,userId, OrderTypeCdConstant.ORDER_TYPE_CD_QUERY,sign,"",null,businesses);
    }


    /**
     * 查询一个服务请求报文
     * @param serviceCode
     * @param serviceName
     * @param paramIn
     * @return
     */
    public static JSONObject createQueryOneBusinessRequestJson(String serviceCode,String serviceName,Map paramIn){
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put("serviceCode",serviceCode);
        business.put("serviceName",serviceName);
        business.getJSONObject("datas").put("params",paramIn);
        return business;
    }



    /**
     * 创建请求 center 报文 分装成JSON
     * @param appId
     * @return
     */
    public static String createCenterServiceRequestJson(String appId,String userId,String orderTypeCd,String sign,String remark,
                                                        JSONArray orderAttrs,JSONArray businesses){
        JSONObject paramIn = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        JSONObject orders = paramIn.getJSONObject("orders");
        orders.put("appId",appId);
        orders.put("transactionId", SequenceUtil.getTransactionId());
        orders.put("userId",userId);
        orders.put("orderTypeCd",orderTypeCd);
        orders.put("requestTime",DateUtil.getNowDefault());
        orders.put("remark",remark);
        if(orderAttrs != null && orderAttrs.size()>0) {
            orders.put("attrs", orderAttrs);
        }
        JSONArray businessArray = paramIn.getJSONArray("business");
        businessArray.addAll(businesses);

        if(!StringUtil.isNullOrNone(sign)) {
            orders.put("sign", AuthenticationFactory.md5(orders.getString("transactionId"), orders.getString("appId"),
                    businessArray.toJSONString(), sign));
        }
        return paramIn.toJSONString();
    }

    /**
     * 数据加密
     * @param reqInfo
     * @param keySize
     * @return
     * @throws Exception
     */
    public static String encrypt(String reqInfo,int keySize) throws Exception {
        return new String(AuthenticationFactory.encrypt(reqInfo.getBytes("UTF-8"), AuthenticationFactory.loadPubKey(MappingConstant.KEY_OUT_PUBLIC_STRING)
                ,keySize),"UTF-8");
    }

    /**
     * 数据解密
     * @param resInfo
     * @param keySize
     * @return
     * @throws Exception
     */
    public static String decrypt(String resInfo,int keySize) throws Exception{
        return new String(AuthenticationFactory.decrypt(resInfo.getBytes("UTF-8"), AuthenticationFactory.loadPrivateKey(MappingConstant.KEY_OUT_PRIVATE_STRING)
                ,keySize),"UTF-8");
    }
}
