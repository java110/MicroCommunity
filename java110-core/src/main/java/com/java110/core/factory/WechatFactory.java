package com.java110.core.factory;

import java.util.Date;

/**
 * @ClassName WechatFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/13 22:15
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
public class WechatFactory {

    public static String formatText(String toUserName, String fromUserName, String content) {
        String str = "";
        str = String.format("<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%4$s]]></Content><FuncFlag>0</FuncFlag></xml>", new Object[]{
                fromUserName, toUserName, Long.valueOf((new Date()).getTime()), content
        });
        return str;
    }
}
