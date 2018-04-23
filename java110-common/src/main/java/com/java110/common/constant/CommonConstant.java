package com.java110.common.constant;

/**
 * 公共常量定义
 * Created by wuxw on 2016/12/28.
 */
public class CommonConstant {

    public final static String SAVE_DATA_ERROR = "101";

    /**
     * 撤单后缀，为了选用不同的事件去处理
     */
    public final static String SUFFIX_DELETE_ORDER = "D";

    /**
     * 同步方式处理
     */
    public final static String PROCESS_ORDER_SYNCHRONOUS = "S";

    /**
     * 异步方式处理
     */
    public final static String PROCESS_ORDER_ASYNCHRONOUS = "A";

    public final static String ORDER_INVOKE_METHOD_SYNCHRONOUS = "S"; //同步
    public final static String ORDER_INVOKE_METHOD_ASYNCHRONOUS = "A"; //异步


    public final static String INVOKE_BUSINESS_MODEL_LOCAL = "LOCAL_SERVICE"; //调用本地

    public final static String QUERY_MODEL_SQL = "1";
    public final static String QUERY_MODEL_PROC = "2";
    public final static String QUERY_MODE_JAVA = "3";

    /**
     * 加密 开关
     */
    public final static String ENCRYPT_ON_OFF = "ENCRYPT";

    public final static String ENCRYPT_KEY_SIZE = "ENCRYPT_KEY_SIZE";

}
