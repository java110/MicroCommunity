package com.java110.utils.constant;

/**
 * 映射表配置
 * Created by wuxw on 2018/4/14.
 */
public final class MappingConstant {

    private MappingConstant() {

    }


    // 系统开关
    public static final String DOMAIN_SYSTEM_SWITCH = "SYSTEM_SWITCH";

    // 短信配置
    public static final String SMS_DOMAIN = "SMS_DOMAIN";

    // 存储配置
    public static final String FILE_DOMAIN = "FILE_DOMAIN";
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";
    public static final String FILE_OSS = "OSS";

    // 平台URL
    public static final String URL_DOMAIN = "URL_DOMAIN";

    // 微信商户
    public static final String WECHAT_STORE_DOMAIN = "WECHAT_STORE";


    // 商城公众号
    public static final String MALL_WECHAT_DOMAIN = "MALL_WECHAT";

    // 工单配置
    public static final String REPAIR_DOMAIN = "REPAIR_DOMAIN";

    //环境配置
    public static final String ENV_DOMAIN = "ENV_DOMAIN";


    //飞蛾小票机配置
    public static final String FEIE_DOMAIN = "FEIE_DOMAIN";

    //拓强智能电表 域
    public static final String TDDIANBIAO_DOMAIN = "TDDIANBIAO_DOMAIN";

    public  static final String KEY_LOG_ON_OFF = "LOG_ON_OFF";

    public static final String KEY_COST_TIME_ON_OFF = "COST_TIME_ON_OFF";

    //私钥
    public static final String KEY_PRIVATE_STRING = "PRIVATE_STRING";

    //公钥
    public static final String KEY_PUBLIC_STRING = "PUBLIC_STRING";

    //私钥(外部系统)
    public static final String KEY_OUT_PRIVATE_STRING = "OUT_PRIVATE_STRING";

    //公钥(外部系统)
    public static final String KEY_OUT_PUBLIC_STRING = "OUT_PUBLIC_STRING";
    //解密 KEY_SIZE
    public static final String KEY_DEFAULT_DECRYPT_KEY_SIZE = "DEFAULT_DECRYPT_KEY_SIZE";

    public static final String KEY_RULE_ON_OFF = "RULE_ON_OFF";
    public static final String VALUE_ON = "ON";
    public static final String VALUE_OFF = "OFF";

    //不用调用规则校验的配置
    public static final String KEY_NO_NEED_RULE_VALDATE_ORDER = "NO_NEED_RULE_VALDATE_ORDER";//Q

    //不用保存订单也订单项信息的配置
    public static final String KEY_NO_SAVE_ORDER = "NO_SAVE_ORDER";//Q

    // 不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public static final String KEY_NO_INVOKE_BUSINESS_SYSTEM = "NO_INVOKE_BUSINESS_SYSTEM";//

    // 不用调用 作废下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public static final String KEY_NO_INVALID_BUSINESS_SYSTEM = "NO_INVALID_BUSINESS_SYSTEM";//

    //需要调用服务生成各个ID
    public static final String KEY_NEED_INVOKE_GENERATE_ID = "NEED_INVOKE_SERVICE_GENERATE_ID";

    //默认掩码
    public static final String KEY_DEFAULT_SECURITY_CODE = "DEFAULT_SECURITY_CODE";

    // 生成 编码路径
    public static final String KEY_CODE_PATH = "CODE_PATH";


    /**
     * 中心服务地址
     */
    public static final String KEY_CENTER_SERVICE_URL = "CENTER_SERVICE_URL";

    public static final String KEY_API_SERVICE_URL = "API_SERVICE_URL";

    /**
     * 控制中心服务APP_ID
     */
    public static final String KEY_CONSOLE_SERVICE_APP_ID = "CONSOLE_SERVICE_APP_ID";

    public static final String KEY_CONSOLE_SECURITY_CODE = "CONSOLE_SECURITY_CODE";

    /**
     * 控制服务是否调用接口解密处理
     */
    public static final String KEY_CONSOLE_SERVICE_SECURITY_ON_OFF = "CONSOLE_SERVICE_SECURITY_ON_OFF";

    /**
     * 用户秘钥
     */
    public static final String KEY_USER_PASSWORD_SECRET = "USER_PASSWORD_SECRET";

    /**
     * JWT 秘钥
     */
    public static final String KEY_JWT_SECRET = "JWT_SECRET";

    /**
     * JWT 秘钥
     */
    public static final String KEY_JWT_EXPIRE_TIME = "JWT_EXPIRE_TIME";

    /**
     * 员工默认密码
     */
    public static final String KEY_STAFF_DEFAULT_PASSWORD = "STAFF_DEFAULT_PASSWORD";


    /**
     * 默认权限
     */
    public static final String DOMAIN_DEFAULT_PRIVILEGE_ADMIN = "DEFAULT_PRIVILEGE_ADMIN";


    /**
     * 默认权限
     */
    public static final String DOMAIN_DEFAULT_PRIVILEGE = "DEFAULT_PRIVILEGE";


    /**
     * 商户类型转 小区成员角色
     */
    public static final String DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE = "STORE_TYPE_2_COMMUNITY_MEMBER_TYPE";


    /**
     * 小区成员审核
     */
    public static final String DOMAIN_COMMUNITY_MEMBER_AUDIT = "COMMUNITY_MEMBER_AUDIT";


}
