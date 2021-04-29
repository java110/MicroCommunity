package com.java110.utils.util;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.log.LoggerEngine;

import com.java110.utils.namespace.NameSpaceHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存管理类
 * Created by wuxw on 2017/3/2.
 * version:1.0
 */
public class CacheUtil extends LoggerEngine {

    public static final String KEY_RESPONSE_MESSAGE_PREFIX = "RESPONSE_MESSAGE_"; // 回复语 key前缀

    public static final String KEY_CODE_MAPPING_PREFIX = "CODE_MAPPING_"; // 映射表key前缀

    public static final String KEY_OFFER_SPEC_PREFIX = "KEY_OFFER_SPEC_";//销售品规格 key前缀,单个销售品用

    public static final String KEY_OFFER_SPEC_PAGE_PREFIX = "KEY_OFFER_SPEC_PAGE_";//销售品规格 key前缀，主要用户存放分页后的数据

    public static final String KEY_USER_PREFIX = "KEY_USER_";//用户信息key前缀


    public static final String IS_START_CUSTOM_CACHE_SYSTEM = "1";//CustomizedPropertyPlaceholderConfigurer.getContextProperty("global.custom_cached_system").toString();

    public static final String CUSTOM_CACHE_ON = "ON";//

    public static final String CUSTOM_CACHE_OFF = "OFF";

    public static int set(String key, Object object) {
        return set(key, JSONObject.toJSONString(object));
    }


    public static int set(String key, Object object,int expireTime) {
        return set(NameSpaceHandler.getDefaultNamespace(),key, JSONObject.toJSONString(object),expireTime);
    }

    public static int set(int namespace,String key, Object object,int expireTime) {
        return set(NameSpaceHandler.getDefaultNamespace(),key, JSONObject.toJSONString(object),expireTime);
    }


    /**
     * 保存一个字符串至缓存中
     *
     * @param value
     * @return
     */
    public static int set(String key, String value) {

        return set(key,value,0);
    }

    /**
     * 保存数据（有有效时间）
     * @param key
     * @param value
     * @param expireTime 有效时间
     * @return
     */
    public static int set(int namespace,String key,String value,int expireTime){

        return 0;
    }

    /**
     * 保存一个List对象至缓存
     *
     * @param key
     * @param values
     * @return
     */
    public static int sets(String key, List<String> values) {

        return 0;
    }

    /**
     * 客户端调用
     *
     * @param key
     * @param objects
     * @param
     * @param <T>
     * @return
     */
    public static <T> int sets(String key, Object objects) {
        List<T> ts = (List<T>) objects;
        List<String> datas = new ArrayList<String>();
        for (T tmp : ts) {
            datas.add(JSONObject.toJSONString(tmp));
        }
        return sets(key, datas);
    }

    /**
     * 从缓存中根据key获取对象 查询对象，对象必须实现序列化接口
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T get(int namespace,String key){
        return get(namespace,key,null);
    }

    /**
     * 从缓存中根据key获取对象 查询对象，对象必须实现序列化接口
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T get(int namespace,String key,Class<T> t) {

        return null;
    }

    /**
     * 从缓存中根据key获取对象 查询对象，对象必须实现序列化接口
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T get(String key) {
        return get(NameSpaceHandler.getDefaultNamespace(),key);
    }

    /**
     * 从缓存中根据key获取对象
     * 从缓存中获取数据，默认是List<String>数据
     *
     * @param key
     * @return
     */
    public static List<String> gets(String key) {

        return null;
    }

    /**
     * 根据指定key 查询数据转为相应对象
     * 主要，在存储数据时 不通对象类型key 不能重复，不然获取时会出现类型转换失败错误
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> gets(String key, Class<T> t) {

        List<String> datas = gets(key);

        if (datas == null) {
            return null;
        }

        List<T> ts = new ArrayList<T>();

        for (String data : datas) {
            T t1 = JSONObject.parseObject(data, t);
            ts.add(t1);
        }

        return ts;
    }

    /**
     * 删除默认namespace 的key
     * @param key
     * @return
     */
    public static int delete(String key){
        return delete(NameSpaceHandler.getDefaultNamespace(),key);
    }

    /**
     * 删除制定namespace 的key
     * @param key
     * @return
     */
    public static int delete(int namespace,String key){

        return 0;
    }
}
