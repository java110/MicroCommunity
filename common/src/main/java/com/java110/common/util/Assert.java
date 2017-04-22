package com.java110.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
    public static void isNull(JSONObject jsonObject,String key,String message){
        Assert.isNull(jsonObject,message);

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

        if(jsonArray.size() == 0 ){
            throw new IllegalArgumentException(message) ;
        }
    }
}
