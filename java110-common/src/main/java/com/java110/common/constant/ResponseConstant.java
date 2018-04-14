package com.java110.common.constant;

/**
 * Created by wuxw on 2016/12/28.
 */
public class ResponseConstant {


    /**
     * 结果返回成功
     */
    public final static String RESULT_CODE_SUCCESS = "0000";

    /**
     * 失败，通用失败编码
     */
    public final static String RESULT_CODE_ERROR = "1999";

    /**
     * 失败，通用失败编码
     */
    public final static String RESULT_CODE_INNER_ERROR = "1998";

    /**
     * 失败，通用失败编码
     */
    public final static String RESULT_CODE_TIME_OUT_ERROR = "1997";

    /**
     * 失败，入参错误，为空或格式错误
     */
    public final static String RESULT_CODE_NO_AUTHORITY_ERROR = "1996";

    public final static String RESULT_CODE_RULE_ERROR = "1995";

    /**
     * 没有从报文中获取到 请求流水
     */
    public final static String NO_TRANSACTION_ID = "-1";
    public final static String NO_NEED_SIGN = "";
}
