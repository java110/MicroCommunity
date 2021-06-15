package com.java110.utils.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.log.LoggerEngine;
import com.java110.entity.protocol.SvcCont;
import  com.java110.entity.protocol.TcpCont;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ProtocolUtil 类 定义 服务之间协议
 * TcpCont头部协议
 * SvcCont内容协议
 * 此协议为JSON格式协议
 * {
 * "tcpCont":{
 * "transactionId":"1000000001201702145112526000",
 * "serviceCode":"FS_001",
 * "reqTime":"20170214112526",
 * },
 * "svcCont":{
 * .....各个交互对象
 * }
 * }
 * transactionId 表示交易流水
 * serviceCode 服务类型，FS_001 用户查询服务 FS_002 用户增加服务
 * <p>
 * Created by wuxw on 2017/2/27.
 */
public class ProtocolUtil {

    public static final String SERVICE_CODE_USER_QUERY = "FS_001"; //用户查询服务

    public static final String SERVICE_CODE_USER_SAVE = "FS_002"; //用户保存

    public static final String SERVICE_CODE_USER_DELETE = "FS_003"; //删除用户

    public static final String SERVICE_CODE_USER_DEL = "FS_003";//用户删除

    public static final String SERVICE_CODE_INIT_CACHE = "FS_004";//加载缓存

    public static final String SERVICE_CODE_SEND_CODE = "FS_005";//发送验证码

    public static final String SERVICE_CODE_OFFER_QUERY = "FS_006";//销售品查询

    public static final String SERVICE_CODE_ADD_OFFER = "FS_007"; //销售品订购

    public static final String RESULT_CODE = "RESULT_CODE"; //接口返回 编码key

    public static final String RESULT_MSG = "RESULT_MSG"; // 接口返回 描述key

    public static final String RESULT_INFO = "RESULT_INFO"; //接口返回 信息 key


    private static JSONObject tcpContJson = null;

    private static JSONObject svcContJson = null;

    private static JSONObject requestJson = null;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    public static final String RETURN_MSG_ERROR = "1999";//通用错误

    public static final String RETURN_MSG_REMOTE_TIMEOUT = "1998";// 调用远程服务超时，失败

    public static final String RETURN_MSG_SUCCESS = "0000"; //处理成功

    /**
     * 创建请求报文头部信息
     *
     * @param serviceCode
     * @return
     */
    public static JSONObject createRequestTcpCont(String serviceCode) {
        return createRequestTcpContJson(serviceCode);
    }

    public static JSONObject createRequestTcpContJson(String serviceCode) {
        //获取交易流水
        String transactionId = "-1";
        tcpContJson = new JSONObject();
        tcpContJson.put("transactionId", transactionId);
        tcpContJson.put("serviceCode", serviceCode);
        tcpContJson.put("reqTime", dateFormat.format(new Date()));
        return tcpContJson;
    }

    /**
     * 创建报文体信息
     *
     * @param objs
     * @return
     */
    public static String createSvcCont(Object... objs) {
        JSONArray svcContArray = new JSONArray();
        for (int objIndex = 0; objIndex < objs.length; objIndex++) {
            svcContArray.add(JSONObject.parseObject(JSONObject.toJSONString(objs[objIndex])));
        }
        return svcContArray.toString();
    }

    public static JSONArray createSvcContJsonArray(Object... objs) {
        return JSONArray.parseArray(createSvcCont(objs));
    }

    /**
     * 创建请求报文(返回JSONObject)
     *
     * @param serviceCode 服务编码
     * @param objs        请求对象，多个
     * @return
     */
    public static JSONObject createRequestJsonObject(String serviceCode, Object... objs) {
        requestJson = new JSONObject();
        requestJson.put("tcpCont", createRequestTcpCont(serviceCode));

        if (objs == null || objs.length == 0) {
            requestJson.put("svcCont", "");
        } else {
            requestJson.put("svcCont", createSvcContJsonArray(objs));
        }
        return requestJson;
    }

    /**
     * 创建请求报文(返回String)
     *
     * @param serviceCode 服务编码
     * @param objs        请求对象，多个
     * @return
     */
    public static String createRequestJsonString(String serviceCode, Object... objs) {
        return createRequestJsonObject(serviceCode, objs).toString();
    }

    /**
     * 创建返回报文头部信息
     *
     * @param serviceCode
     * @return
     */
    public static JSONObject createResponseTcpCont(String transactionId, String serviceCode, String resultCode, String resultMsg) {
        return createResponseTcpContJson(transactionId, serviceCode, resultCode, resultMsg);
    }

    public static JSONObject createResponseTcpContJson(String transactionId, String serviceCode, String resultCode, String resultMsg) {
        tcpContJson = new JSONObject();
        tcpContJson.put("transactionId", transactionId);
        tcpContJson.put("serviceCode", serviceCode);
        tcpContJson.put("resTime", dateFormat.format(new Date()));
        tcpContJson.put("resultCode", resultCode);
        tcpContJson.put("resultMsg", resultMsg);
        return tcpContJson;
    }


    /**
     * 创建返回报文(返回JSONObject)
     *
     * @param serviceCode 服务编码
     * @param objs        请求对象，多个
     * @return
     */
    public static JSONObject createResponseJsonObject(String transactionId, String serviceCode, String resultCode, String resultMsg, Object... objs) {
        requestJson = new JSONObject();
        requestJson.put("tcpCont", createResponseTcpCont(transactionId, serviceCode, resultCode, resultMsg));
        if (objs == null || objs.length == 0) {
            requestJson.put("svcCont", "");
        } else {
            requestJson.put("svcCont", createSvcContJsonArray(objs));
        }
        return requestJson;
    }

    /**
     * 创建返回报文(返回String)
     *
     * @param serviceCode 服务编码
     * @param objs        请求对象，多个
     * @return
     */
    public static String createResponseJsonString(String transactionId, String serviceCode, String resultCode, String resultMsg, Object... objs) {
        return createResponseJsonObject(transactionId, serviceCode, resultCode, resultMsg, objs).toString();
    }

    /**
     * 根据请求报文创建返回报文（返回 String ）
     *
     * @param jsonString 请求报文
     * @param resultCode 返回编码
     * @param resultMsg  返回信息描述
     * @param objs       返回对象
     * @return
     */
    public static String createResponseJsonString(String jsonString, String resultCode, String resultMsg, Object... objs) {
        TcpCont tcpCont = getTcpCont(jsonString);
        return createResponseJsonString(tcpCont.getTransactionId(), tcpCont.getServiceCode(), resultCode, resultMsg, objs);
    }


    /**
     * 将报文头部信息放入封装成TcpCont对象
     *
     * @param jsonString 报文
     * @return
     */
    public static TcpCont getTcpCont(String jsonString) {
        //校验空
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        tcpContJson = JSONObject.parseObject(jsonString).getJSONObject("tcpCont");
        return JSONObject.parseObject(tcpContJson.toString(), TcpCont.class);
    }

    /**
     * 将报文体内容分装到SvcCont对象
     *
     * @param jsonString 报文
     * @return
     */
    public static SvcCont getSvcCont(String jsonString) {
        JSONArray svcContArray = JSONObject.parseObject(jsonString).getJSONArray("svcCont");
        if (svcContArray == null || svcContArray.size() == 0) {
            return null;
        }
        List<JSONObject> objs = new ArrayList<JSONObject>();
        for (int objIndex = 0; objIndex < svcContArray.size(); objIndex++) {
            objs.add(svcContArray.getJSONObject(objIndex));
        }
        SvcCont svcCont = new SvcCont();
        svcCont.setObjs(objs);
        return svcCont;
    }

    /**
     * 从报文中获取指定的对象
     *
     * @param svcCont
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T getObject(SvcCont svcCont, Class<T> c) {
        if (svcCont == null || svcCont.getObjs() == null || svcCont.getObjs().size() == 0) {
            return null;
        }
        for (JSONObject obj : svcCont.getObjs()) {
            if (c.getClass().getName().equals(obj.getString("beanName"))) {
                return JSONObject.toJavaObject(obj, c);
            }
        }
        return null;
    }

    /**
     * 从报文中获取指定的对象（list 方式）
     *
     * @param svcCont
     * @param c
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjects(SvcCont svcCont, Class<T> c) {
        if (svcCont == null || svcCont.getObjs() == null || svcCont.getObjs().size() == 0) {
            return null;
        }
        List<T> ts = new ArrayList<T>();
        for (JSONObject obj : svcCont.getObjs()) {
            if (c.getClass().getName().equals(obj.getString("beanName"))) {
                ts.add(JSONObject.toJavaObject(obj, c));
            }
        }
        return ts;
    }

    /**
     * 从报文中获取指定的对象
     *
     * @param jsonString
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T getObject(String jsonString, Class<T> c) {
        SvcCont svcCont = getSvcCont(jsonString);
        if (svcCont == null || svcCont.getObjs() == null || svcCont.getObjs().size() == 0) {
            return null;
        }
        for (JSONObject obj : svcCont.getObjs()) {
            if (obj !=null && c.getName().equals(obj.getString("beanName"))) {
                return JSONObject.toJavaObject(obj, c);
            }
        }
        return null;
    }

    /**
     * 从报文中获取指定的对象（list 方式）
     *
     * @param jsonString 请求字符串
     * @param c
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjects(String jsonString, Class<T> c) {
        SvcCont svcCont = getSvcCont(jsonString);
        if (svcCont == null || svcCont.getObjs() == null || svcCont.getObjs().size() == 0) {
            return null;
        }
        List<T> ts = new ArrayList<T>();
        for (JSONObject obj : svcCont.getObjs()) {
            if (c.getClass().getName().equals(obj.getString("beanName"))) {
                ts.add(JSONObject.toJavaObject(obj, c));
            }
        }
        return ts;
    }



    /**
     * 创建公用输出
     *  Controller返回 html 协议
     * {'RESULT_CODE':'1999','RESULT_MSG':'失败原因','RESULT_INFO':{...}}
     * @return
     */
    public static String createResultMsg(String resultCode, String resultMsg, JSONObject resultInfo) {
        JSONObject data = new JSONObject();
        data.put(RESULT_CODE, resultCode);
        data.put(RESULT_MSG, resultMsg);
        data.put(RESULT_INFO, resultInfo);
        return data.toString();
    }

    /**
     * 校验返回报文
     * 如果为true 成功
     * false 失败
     * @param returnJsonParam
     * @return
     */
    public static boolean validateReturnJson(String returnJsonParam,JSONObject paramJson){
        try{
             paramJson = JSONObject.parseObject(returnJsonParam);

            if(paramJson == null || !paramJson.containsKey(RESULT_CODE)){
                return false;
            }

            if(ProtocolUtil.RETURN_MSG_SUCCESS.equals(paramJson.getString(RESULT_CODE))){
                return true;
            }

        }catch (Exception e){
            LoggerEngine.error("返回报文必须为json格式，当前格式错误 " + returnJsonParam);
            return false;
        }
        return false;
    }
}
