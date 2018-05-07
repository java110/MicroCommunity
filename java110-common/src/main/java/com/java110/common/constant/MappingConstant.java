package com.java110.common.constant;

/**
 * 映射表配置
 * Created by wuxw on 2018/4/14.
 */
public class MappingConstant {




    public  final static String KEY_LOG_ON_OFF = "LOG_ON_OFF";
    public  final static String KEY_COST_TIME_ON_OFF = "COST_TIME_ON_OFF";

    //私钥
    public final static String KEY_PRIVATE_STRING = "PRIVATE_STRING";

    //公钥
    public final static String KEY_PUBLIC_STRING = "PUBLIC_STRING";

    //私钥(外部系统)
    public final static String KEY_OUT_PRIVATE_STRING = "OUT_PRIVATE_STRING";

    //公钥(外部系统)
    public final static String KEY_OUT_PUBLIC_STRING = "OUT_PUBLIC_STRING";
    //解密 KEY_SIZE
    public final static String KEY_DEFAULT_DECRYPT_KEY_SIZE = "DEFAULT_DECRYPT_KEY_SIZE";

    public  final static String KEY_RULE_ON_OFF = "RULE_ON_OFF";
    public  final static String VALUE_ON = "ON";
    public  final static String VALUE_OFF = "OFF";

    //不用调用规则校验的配置
    public  final static String KEY_NO_NEED_RULE_VALDATE_ORDER = "NO_NEED_RULE_VALDATE_ORDER";//Q

    //不用保存订单也订单项信息的配置
    public  final static String KEY_NO_SAVE_ORDER = "NO_SAVE_ORDER";//Q

    // 不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public  final static String KEY_NO_INVOKE_BUSINESS_SYSTEM = "NO_INVOKE_BUSINESS_SYSTEM";//

    // 不用调用 作废下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public  final static String KEY_NO_INVALID_BUSINESS_SYSTEM = "NO_INVALID_BUSINESS_SYSTEM";//

    //需要调用服务生成各个ID
    public final static String KEY_NEED_INVOKE_GENERATE_ID = "NEED_INVOKE_SERVICE_GENERATE_ID";

    //默认掩码
    public final static String KEY_DEFAULT_SECURITY_CODE = "DEFAULT_SECURITY_CODE";


    /**
     * 中心服务地址
     */
    public final static String KEY_CENTER_SERVICE_URL = "CENTER_SERVICE_URL";

    /**
     * 控制中心服务APP_ID
     */
    public final static String KEY_CONSOLE_SERVICE_APP_ID = "CONSOLE_SERVICE_APP_ID";

    public final static String KEY_CONSOLE_SECURITY_CODE = "CONSOLE_SECURITY_CODE";

    /**
     * 控制服务是否调用接口解密处理
     */
    public final static String KEY_CONSOLE_SERVICE_SECURITY_ON_OFF = "CONSOLE_SERVICE_SECURITY_ON_OFF";

    /**
     * 用户秘钥
     */
    public final static String KEY_USER_PASSWORD_SECRET = "USER_PASSWORD_SECRET";

    /**
     * JWT 秘钥
     */
    public final static String KEY_JWT_SECRET = "JWT_SECRET";

    /**
     * JWT 秘钥
     */
    public final static String KEY_JWT_EXPIRE_TIME = "JWT_EXPIRE_TIME";



}
