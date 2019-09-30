package com.java110.utils.util;


/**
 * 常量定义管理类
 * Created by wuxw on 2017/3/14.
 */
public class ConstantUtil {

    /**
     * 能力平台请求时的授权码
     */
    public static final String SAOP_SRC_SYS_SIGN="SrcSysSign";

    /**
     * 能力平台源组织ID
     */
    public static final String SAOP_SRC_ORG_ID="SrcOrgID";

    /**
     * 源系统ID
     */
    public static final String SAOP_SRC_SYS_ID="SrcSysID";

    /**
     * 目标组织ID
     */
    public static final String SAOP_DST_ORG_ID = "DstOrgID";
    /**
     * 目标系统ID
     */
    public static final String SAOP_DST_SYS_ID = "DstSysID";

    /**
     * 渠道编码
     */
    public static final String SAOP_CHANNEL_NBR = "CHANNEL_NBR";
    /**
     * 员工编码
     */
    public static final String SAOP_STAFF_CODE = "STAFF_CODE";


    /**
     * 根据号码查询用户信息报文体获取
     */
    public static final String SAOP_GET_CUST_INFO_BY_ACC_NBR = "getCustInfoListByAccNbr";

    /**
     * 能力向省内提供接口地址
     */
    public static final String SAOP_PROVINCE_WEBSERVICE_URL = "saop_province_webservice_url";

    /**
     * 增加购物车属性配置，这里只支持固定值得
     */
    public static final String SAOP_ADD_CUSTOMER_ORDER_ATTR = "ADD_CUSTOMER_ORDER_ATTR";

    /**
     * 修改购物车属性配置，这里只支持固定值得
     */
    public static final String SAOP_MOD_CUSTOMER_ORDER_ATTR = "MOD_CUSTOMER_ORDER_ATTR";


    /**
     * 能力平台向省内提供的接口方法
     */
    public static final String SAOP_PROVINCE_FUNCTION = "exchangeForProvince";

    public static final String SAOP_EXCHANGE_FUNCTION = "exchange";

    /**
     * 短信域，只要用户发送验证码
     */
    public static final String DOMAIN_SMS = "sms";

    /**
     * 短信相关信息
     */
    public static final String SMS_USERNAME = "username";
    public static final String SMS_PASSWORD = "password";
    public static final String SMS_SYSCODE = "syscode";
    public static final String SMS_PRODUCT_ID = "productId";
    public static final String SMS_CHANNEL_TYPE = "channelType";
    public static final String SMS_FLOW_CODE = "flowCode";
    public static final String SMS_SENT_TYPE = "sentType";
    public static final String SMS_BUSINESS_ID = "businessId";
    public static final String SMS_CREATE_DESPART = "createDespart";
    public static final String SMS_CREATE_STAFF = "createStaff";
    public static final String SMS_PLAN_ID = "planId";
    public static final String SMS_PUSH_URL = "pushUrl";

    /**
     * 短信验证码，短信模板
     */
    public static final String SMS_SEND_CODE_CONTENT="sendCodeContent";

    /**
     * 短信平台接口地址
     */
    public static final String SMS_SEND_CODE_WEBSERVICE_URL = "sms_send_code_webservice_url";

    /**
     * 短信平台接口方法
     */
    public static final String SMS_SEND_CODE_FUNCTION = "sms_send_code_function";


    //服务交互报文保存开关 ON Or OFF
    public static final String SAVE_SEVICE_LOG_FLAG = "save_sevice_log_flag";


    /**
     * 销售品订购，退订 调用4G接口
     */
    public static final String OFFER_CATALOG_4G = "4G";


    /**
     * 销售品订购，退订 调用3G接口
     */
    public static final String OFFER_CATALOG_3G = "3G";


    /**
     * 销售品订购，退订 调用EOP接口
     */
    public static final String OFFER_CATALOG_EOP = "EOP";







}
