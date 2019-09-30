package com.java110.core.base.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.log.LoggerEngine;
import com.java110.core.base.AppBase;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 负责和数据库交互基类
 * 分装数据库相关方法
 *
 * Created by wuxw on 2017/2/28.
 */
public class BaseServiceDao extends AppBase {

   /* @Autowired
    protected JedisPool jedisPool;*/


    @Autowired
   protected SqlSessionTemplate sqlSessionTemplate;


    public SqlSessionTemplate getSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

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
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION+"请求报文格式错误String无法转换为JSONObjcet对象：", e);
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
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION+"请求报文格式错误String无法转换为JSONObjcet对象：", e);
        } finally {
            LoggerEngine.debug("报文简单校验simpleValidateJSON结束，出参为：" , reqMap);
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
            reqMap = JSONArray.parseArray(jsonParam,Map.class);
        } catch (Exception e) {
            //抛出转json异常
            throw new RuntimeException(SERVICE_CASE_JSON_EXCEPTION+"请求报文格式错误String无法转换为JSONArray对象：", e);
        } finally {
            LoggerEngine.debug("报文简单校验simpleValidateJSON结束，出参为：" , reqMap);
        }
        return reqMap;
    }

  /*  public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }*/
}
