package com.java110.common.activity;

import org.activiti.engine.impl.cfg.IdGenerator;

import java.util.UUID;

/**
 * @ClassName ActivityIdGenerator
 * @Description TODO
 * @Author wuxw
 * @Date 2019/10/22 21:56
 * @Version 1.0
 * add by wuxw 2019/10/22
 **/
public class ActivityIdGenerator implements IdGenerator {
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Activiti ID 生成
     */
    @Override
    public String getNextId() {
        return uuid();
    }

}
