package com.java110.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 自定义 断言
 * Created by wuxw on 2017/4/22.
 */
public class Assert extends org.springframework.util.Assert{

    /**
     * 判断 jsonObject 是否为空
     * @param jsonObject
     * @param key
     * @param message
     */
    public static void isNotNull(JSONObject jsonObject,String key,String message){
        Assert.notEmpty(jsonObject,message);

        if(!jsonObject.containsKey(key)){
            throw new IllegalArgumentException(message) ;
        }
    }

    /**
     * 判断json是否为空
     * @param jsonArray
     * @param message
     */
    public static void isNull(JSONArray jsonArray,String message){

        Assert.isNull(jsonArray,message);

        if(jsonArray.size() < 1 ){
            throw new IllegalArgumentException(message) ;
        }
    }

    /**
     * 判断list 是否为空
     * @param targetList
     * @param message
     */
    public static void isNull(List<?> targetList , String message){

        Assert.isNull(targetList,message);

        if(targetList.size()< 1){
            throw new IllegalArgumentException(message) ;
        }
    }

    /**
     * 判断是否只有一条记录数据
     * @param targetList
     * @param message
     */
    public static void isOne(List<?> targetList,String message){
        Assert.isNull(targetList,message);

        if(targetList.size() != 1){
            throw new IllegalArgumentException(message) ;
        }
    }

    /**
     * 校验map 中是否有值
     * @param targetMap
     * @param message
     */
    public static void hasSize(Map<?,?> targetMap, String message){
        Assert.isNull(targetMap,message);

        if(targetMap.size() < 1){
            throw new IllegalArgumentException(message);
        }

    }

    /**
     * 校验是否为JSON
     * @param msg
     * @return
     */
    public static Boolean isJsonObject(String msg) {
        try{
            JSONObject.parseObject(msg);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static Boolean isPageJsonObject(String msg){
        try{
            JSONObject jsonObject = JSONObject.parseObject(msg);
            if(!jsonObject.containsKey("meta") || !jsonObject.containsKey("param")){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 校验是否为整数
     * @param text
     * @param msg
     */
    public static void isInteger(String text,String msg){
        if(!StringUtils.isNumeric(text)){
            throw new IllegalArgumentException(msg);
        }
    }
}
