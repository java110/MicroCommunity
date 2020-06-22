package com.java110.utils.constant;

/**
 * @ClassName WechatConstant
 * @Description TODO 微信相关常量类
 * @Author wuxw
 * @Date 2020/6/13 22:01
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
public class WechatConstant {

    //微信域
    public static final String WECHAT_DOMAIN = "WECHAT";

    //微信ID
    public static final String PAGE_WECHAT_APP_ID = "wId";//微信id 一般是appId

    public static final String KEY_PROPERTY_FEE_TEMPLATE_ID = "PROPERTY_FEE_TEMPLATE_ID";

    public static final String TOKEN = "TOKEN";
    public static final String WELCOME = "WELCOME";
    public static final String DEFAULT_WELCOME = "HC小区物业管理系统是由java110团队于2017年4月份发起的前后端分离、分布式架构开源项目，目前我们的代码开源在github 和gitee上，开源项目由HC小区管理系统后端，HC小区管理系统前端，HC小区管理系统业主手机版和HC小区管理系统物业手机版，业务技术交流群：827669685";

    public static final String NO_BIND_OWNER_RESPONSE_MESSAGE = "亲，您还没有绑定业主请先<a href=\"https://owner.demo.winqi.cn/#/pages/login/login\">绑定</a>";


    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_LOCATION = "location";
    public static final String MSG_TYPE_VOICE = "voice";
    public static final String MSG_TYPE_VIDEO = "video";
    public static final String MSG_TYPE_IMAGE = "image";
    public static final String MSG_TYPE_EVENT = "event";
    public static final String MSG_REGX_TYPE_PART = "0";
    public static final String MSG_REGX_TYPE_ALL = "1";
    public static final String MSG_MENU_ID = "menu";


    public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

    public static final String APP_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static final String OPEN_AUTH = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URL&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

}
