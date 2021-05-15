package com.java110.core.base.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 抽象服务类
 */
public abstract class AbstractBaseService extends BaseService {

    /**
     * 统一业务处理类
     *
     * @param t 对象分装
     * @return ResponseEntity对象
     */
    public final <T> T businessProcess(T t, Class<?> clazz) {


        return null;

    }
}
