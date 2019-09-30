package com.java110.utils.constant;

/**
 * @author wux
 * @create 2019-02-05 下午11:28
 * @desc 业务信息常量类
 **/
public class ServiceBusinessConstant {

    /**
     * http post 方式 (微服务内应用)
     */
    public final static String INVOKE_TYPE_HTTP_POST = "1";

    /**
     * webservice 方式调用
     */
    public final static String INVOKE_TYPE_WEBSERVICE = "2";

    /**
     * http-post(微服务之外应用)
     */
    public final static String INVOKE_TYPE_OUT_HTTP_POST = "3";
}
