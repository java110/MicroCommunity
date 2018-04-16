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
    public final static String ORDER_INVOKE_METHOD_ASYNCHRONOUS = "A"; //同步

}
