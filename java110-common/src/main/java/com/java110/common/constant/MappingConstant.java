package com.java110.common.constant;

/**
 * 映射表配置
 * Created by wuxw on 2018/4/14.
 */
public class MappingConstant {




    public  final static String KEY_LOG_ON_OFF = "LOG_ON_OFF";
    public  final static String KEY_COST_TIME_ON_OFF = "COST_TIME_ON_OFF";
    public  final static String KEY_RULE_ON_OFF = "RULE_ON_OFF";
    public  final static String VALUE_ON = "ON";
    public  final static String VALUE_OFF = "OFF";

    //不用调用规则校验的配置
    public  final static String KEY_NO_NEED_RULE_VALDATE_ORDER = "NO_NEED_RULE_VALDATE_ORDER";//Q

    //不用报错订单也订单项信息的配置
    public  final static String KEY_NO_SAVE_ORDER = "NO_SAVE_ORDER";//Q

    // 不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public  final static String KEY_NO_INVOKE_BUSINESS_SYSTEM = "NO_INVOKE_BUSINESS_SYSTEM";//

    // 不用调用 作废下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)
    public  final static String KEY_NO_INVALID_BUSINESS_SYSTEM = "NO_INVALID_BUSINESS_SYSTEM";//
}
