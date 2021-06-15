package com.java110.utils.constant;

/**
 * Created by wuxw on 2016/12/28.
 */
public final class ResponseConstant {

    private ResponseConstant() {

    }


    /**
     * 结果返回成功
     */
    public static final String RESULT_CODE_SUCCESS = "0000";

    /**
     * 失败，通用失败编码
     */
    public static final String RESULT_CODE_ERROR = "1999";

    /**
     * 失败，通用失败编码
     */
    public static final String RESULT_CODE_INNER_ERROR = "1998";

    /**
     * 失败，通用失败编码
     */
    public static final String RESULT_CODE_TIME_OUT_ERROR = "1997";

    /**
     * 失败，入参错误，为空或格式错误
     */
    public static final String RESULT_CODE_NO_AUTHORITY_ERROR = "1996";

    public static final String RESULT_CODE_RULE_ERROR = "1995";
    /**
     * 系统配置错误
     */
    public static final String RESULT_CODE_CONFIG_ERROR = "1993";

    /**
     * 参数不正确
     */
    public static final String RESULT_PARAM_ERROR = "1994";

    /**
     * 没有从报文中获取到 请求流水
     */
    public static final String NO_TRANSACTION_ID = "-1";
    public static final String NO_NEED_SIGN = "";
}
