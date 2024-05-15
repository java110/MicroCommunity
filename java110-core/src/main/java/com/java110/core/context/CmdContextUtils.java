package com.java110.core.context;


import com.java110.utils.constant.CommonConstant;

import java.util.Map;

/**
 * cmd 上下文工具类
 */
public class CmdContextUtils {

    /**
     * 查询用户ID
     *
     * @param context
     * @return
     */
    public static final String getUserId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            return headers.get(CommonConstant.HTTP_USER_ID);
        }
        return "";
    }

    /**
     * 查询商户ID
     *
     * @param context
     * @return
     */
    public static final String getStoreId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.STORE_ID)) {
            return headers.get(CommonConstant.STORE_ID);
        }
        return "";
    }

    /**
     * 查询回话ID
     *
     * @param context
     * @return
     */
    public static final String getSessionId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.HTTP_SESSION_ID)) {
            return headers.get(CommonConstant.HTTP_SESSION_ID);
        }
        return "";
    }

    /**
     * 查询 AppId
     *
     * @param context
     * @return
     */
    public static final String getAppId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.APP_ID)) {
            return headers.get(CommonConstant.APP_ID);
        }
        return "";
    }


    /**
     * 查询 登录用户ID
     *
     * @param context
     * @return
     */
    public static final String getLoginUserId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.LOGIN_U_ID)) {
            return headers.get(CommonConstant.LOGIN_U_ID);
        }
        return "";
    }

    /**
     * 查询 登录用户ID
     *
     * @param context
     * @return
     */
    public static final String getTransactionId(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.TRANSACTION_ID)) {
            return headers.get(CommonConstant.TRANSACTION_ID);
        }
        return "";
    }

    public static final String getRequestTime(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.REQUEST_TIME)) {
            return headers.get(CommonConstant.REQUEST_TIME);
        }
        return "";
    }

    public static final String getLang(ICmdDataFlowContext context) {
        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.LANG_ZH_CN)) {
            return headers.get(CommonConstant.LANG_ZH_CN);
        }
        return "";
    }


    public static String getTokenId(ICmdDataFlowContext context) {


        if (context == null) {
            return "";
        }

        Map<String, String> headers = context.getReqHeaders();

        if (headers.containsKey(CommonConstant.COOKIE_AUTH_TOKEN)) {
            return headers.get(CommonConstant.COOKIE_AUTH_TOKEN);
        }
        return "";
    }
}
