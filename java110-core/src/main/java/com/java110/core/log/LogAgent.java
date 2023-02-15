package com.java110.core.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.KafkaConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.Assert;
import com.java110.core.context.DataFlow;
import com.java110.core.context.TransactionLog;

import java.util.Map;

/**
 * 日志代理
 * 收集日志 发送至 日志服务
 * Created by wuxw on 2018/6/9.
 */
public class LogAgent extends LoggerEngine{

    public static final String LOG_STATUS_S = "S";
    public static final String LOG_STATUS_F = "F";




    /**
     * 发送日志
     * @param transactionLog
     * @return
     */
    public static boolean sendLog(TransactionLog transactionLog){
        if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_LOG_ON_OFF))) {
            try {
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME, "", transactionLog.toString());
            }catch (Exception e){
                logger.error("保存日志失败："+transactionLog.toString(),e);
                return false;
            }
        }
        return true;
    }


    /**
     * 发送交互日志
     * @param dataFlow 数据流对象
     * @param requestMessage 请求数据
     * @param responseMessage 返回数据
     * @param logStatus 日志状态
     * @return
     */
    public static boolean sendLog(DataFlow dataFlow,String requestMessage,String responseMessage,String logStatus,long costTime){
        return sendLog(dataFlow.reBuilder(requestMessage,responseMessage,logStatus,costTime));
    }

    /**
     * 发送交互日志
     * 请求报文和返回报文必须组装成
     * {"headers":"",
     * "body":""
     * }
     * @param dataFlow 数据流对象
     * @param requestMessage 请求数据
     * @param responseMessage 返回数据
     * @return
     */
    public static boolean sendLog(DataFlow dataFlow, JSONObject requestMessage, JSONObject responseMessage,long costTime){
        Assert.hasKey(responseMessage,"body","返回报文不满足 日志协议要求"+responseMessage.toJSONString());

        String body = responseMessage.getString("body");
        String logStatus = LOG_STATUS_F;
        //如果是JSONObject
        if(Assert.isJsonObject(body)){
            JSONObject bodyObj = JSONObject.parseObject(body);

            Object codeNode = JSONPath.eval(bodyObj,"$.orders.response.code");
            //判断订单是否成功
            if(codeNode != null && ResponseConstant.RESULT_CODE_SUCCESS.equals(codeNode.toString())){
                //判断业务是否受理成功个，如果有一个业务受理失败，则认为失败
                if(!bodyObj.containsKey("business")){
                    return sendLog(dataFlow,requestMessage.toJSONString(),responseMessage.toJSONString(),LOG_STATUS_S,costTime);
                }

                if(bodyObj.get("business") instanceof JSONObject){
                    JSONObject businessObj = bodyObj.getJSONObject("business");
                    if(businessObj.containsKey("response")&&
                            ResponseConstant.RESULT_CODE_SUCCESS.equals(businessObj.getJSONObject("response").getString("code"))){
                        return sendLog(dataFlow,requestMessage.toJSONString(),responseMessage.toJSONString(),LOG_STATUS_S,costTime);
                    }
                }

                if(bodyObj.get("business") instanceof JSONArray){
                    JSONArray businessArrays = bodyObj.getJSONArray("business");
                    if(businessArrays == null || businessArrays.size() == 0){
                        return sendLog(dataFlow,requestMessage.toJSONString(),responseMessage.toJSONString(),LOG_STATUS_S,costTime);
                    }

                    logStatus = LOG_STATUS_S;
                    for(int businessIndex = 0; businessIndex < businessArrays.size();businessIndex ++){
                        JSONObject businessObj = businessArrays.getJSONObject(businessIndex);
                        if(!businessObj.containsKey("response") ||
                                !ResponseConstant.RESULT_CODE_SUCCESS.equals(businessObj.getJSONObject("response").getString("code"))){
                            logStatus = LOG_STATUS_F;
                        }
                    }
                }
            }
        }
        //如果有xml交互，则扩展

        //兼容kafka 传递消息
        if(ResponseConstant.RESULT_CODE_SUCCESS.equals(body)){
            logStatus = LOG_STATUS_S;
        }
        return sendLog(dataFlow,requestMessage.toJSONString(),responseMessage.toJSONString(),logStatus,costTime);
    }


    /**
     * 封装头信息和 消息信息至body中
     * {"headers":"",
     * "body":""
     * }
     * @param headers 头信息
     * @param dataInfo 数据信息
     * @return
     */
    public static JSONObject createLogMessage(Map<String,String> headers, String dataInfo){
        JSONObject message = new JSONObject();
        String headerMessage = (headers == null || headers.isEmpty())?"":JSONObject.toJSONString(headers);
        message.put("headers",headerMessage);
        message.put("body",dataInfo);
        return message;
    }


}
