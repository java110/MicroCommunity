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
    public final static String ENCRYPT = "ENCRYPT";

    public final static String ENCRYPT_KEY_SIZE = "ENCRYPT_KEY_SIZE";

    public final static String MENU_GROUP_LEFT = "LEFT";

    /**
     * 鉴权token
     */
    public final static String COOKIE_AUTH_TOKEN = "_java110_token_";

    public final static String DEFAULT_USER_PWD_SECRET = "@java110.com";

    /**
     * 默认JWT 秘钥
     */
    public final static String DEFAULT_JWT_SECRET = "@java110_JWT";


    /**
     * 默认过期时间
     */
    public final static String DEFAULT_JWT_EXPIRE_TIME = 2*60*60 + "";

    /**
     * 登录时的用户ID
     */
    public final static String LOGIN_USER_ID = "userId";

    public final static String LOGIN_USER_NAME = "userName";

    /**
     * 上下文对象
     */
    public final static String CONTEXT_PAGE_DATA = "pd";

    public final static String ORDER_USER_ID = "ORDER_USER_ID";

    public final static String ORDER_DEFAULT_USER_ID = "-1";

    public final static String TEMPLATE_COLUMN_NAME_BUTTON = "BUTTON";

    public final static String TEMPLATE_URL_LIST = "LIST->";
    public final static String TEMPLATE_URL_QUERY = "QUERY->";
    public final static String TEMPLATE_URL_UPDATE = "UPDATE->";
    public final static String TEMPLATE_URL_DELETE = "DELETE->";
    public final static String TEMPLATE_URL_SPILT = ";";


    public final static String CACHE_PARAM_NAME = "cacheName";

    /**
     * 组件路由 服务 缓存常量
     */
    public final static String CACHE_APP_ROUTE_SERVICE = "APP_ROUTE_SERVICE";

    /**
     * 映射 缓存常量
     */
    public final static String CACHE_MAPPING = "MAPPING";

    /**
     * 映射 缓存常量
     */
    public final static String CACHE_SERVICE_SQL = "SERVICE_SQL";



}
