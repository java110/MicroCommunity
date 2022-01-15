package com.java110.core.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.utils.cache.BaseCache;
import com.java110.utils.log.LoggerEngine;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/4/10.
 */
public class AppBase extends BaseCache {
    private static final Logger logger = LoggerFactory.getLogger(AppBase.class);


    private final static String SERVICE_CASE_JSON_EXCEPTION = "101";//转json异常

    /**
     * 请求报文校验，返回去json
     *
     * @param jsonParam
     * @return
     */
    public JSONObject simpleValidateJSON(String jsonParam) {
        LoggerEngine.debug("报文简单校验simpleValidateJSON开始，入参为：" + jsonParam);

        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.parseObject(jsonParam);
        } catch (Exception e) {
            //抛出转json异常
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION + "请求报文格式错误String无法转换为JSONObjcet对象：", e);
        } finally {
            LoggerEngine.debug("报文简单校验simpleValidateJSON结束，出参为：" + reqJson);
        }

        return reqJson;
    }

    /**
     * 校验请求报文，返回去Map
     *
     * @param jsonParam
     * @return
     */
    public Map<String, Object> simpleValidateJSONReturnMap(String jsonParam) {
        JSONObject reqJson = null;
        Map<String, Object> reqMap = null;
        try {
            reqJson = JSONObject.parseObject(jsonParam);
            reqMap = JSONObject.toJavaObject(reqJson, Map.class);
        } catch (Exception e) {
            //抛出转json异常
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION + "请求报文格式错误String无法转换为JSONObjcet对象：", e);
        } finally {
            LoggerEngine.debug("报文简单校验simpleValidateJSON结束，出参为：", reqMap);
        }
        return reqMap;
    }

    /**
     * 校验请求报文，返回list<Map>
     *
     * @param jsonParam
     * @return
     */
    public List<Map> simpleValidateJSONArrayReturnList(String jsonParam) {
        List<Map> reqMap = null;
        try {
            reqMap = JSONArray.parseArray(jsonParam, Map.class);
        } catch (Exception e) {
            //抛出转json异常
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION + "请求报文格式错误String无法转换为JSONArray对象：", e);
        } finally {
            LoggerEngine.debug("报文简单校验simpleValidateJSON结束，出参为：", reqMap);
        }
        return reqMap;
    }

    /**
     * 返回json对象
     *
     * @param resultCode 返回码
     * @param resultMsg  返回信息（成功或者失败原因）
     * @param info       成功时返回的信息
     * @return
     */
    public String createReturnJson(String resultCode, String resultMsg, String info) {
        JSONObject returnJSON = null;
        returnJSON = new JSONObject();
        returnJSON.put("resultCode", resultCode);
        returnJSON.put("resultMsg", resultMsg);
        returnJSON.put("resultInfo", info);
        return returnJSON.toString();
    }


    /**
     * 返回Map对象
     *
     * @param resultCode
     * @param resultMsg
     * @param info
     * @return
     */
    public Map<String, Object> createReturnMap(String resultCode, String resultMsg, String info) {
        return null;
    }
//
//    /**
//     * 调用中心服务
//     *
//     * @return
//     */
//    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {
//        return CallApiServiceFactory.callCenterService(restTemplate, pd, param, url, httpMethod);
//
//    }

}
