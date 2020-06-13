package com.java110.front.controller;


import com.java110.core.base.controller.BaseController;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.utils.constant.WechatConstant;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @ClassName WechatController
 * @Description TODO 微信 对接类
 * @Author wuxw
 * @Date 2020/6/13 21:48
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
@RestController
@RequestMapping(path = "/app/wechat")
public class WechatGatewayController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(WechatGatewayController.class);


    /**
     * 微信登录接口
     *
     * @param param
     * @param request
     */
    @RequestMapping(path = "/gateway", method = RequestMethod.POST)
    public ResponseEntity<String> gateway(@RequestBody String param, HttpServletRequest request) {
        logger.debug("微信传入信息" + param);


        String token = WechatConstant.TOKEN;
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String responseStr = "";
        logger.debug("token = " + token + "||||" + "signature = " + signature + "|||" + "timestamp = "
                + timestamp + "|||" + "nonce = " + nonce + "|||" + "echostr = " + echostr);
        String sourceString = "";
        String[] ss = new String[]{token, timestamp, nonce};
        Arrays.sort(ss);
        for (String s : ss) {
            sourceString += s;
        }
        String signature1 = AuthenticationFactory.SHA1Encode(sourceString).toLowerCase();
        logger.debug("sourceString = " + sourceString + "||||" + "signature1 = " + signature1);
        if (signature1.equals(signature)) {
            if (echostr == null) {
                String postStr = param;//this.readStreamParameter(request.getInputStream());
                if (postStr == null || postStr.length() == 0) {
                    responseStr = "未输入任何内容";
                } else {
                    /*if (postStr.equals(new String(postStr.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
                        logger.debug(" This type is  iso-8859-1");
                        postStr = new String(postStr.getBytes("ISO-8859-1"), "UTF-8");

                    }
                    if (postStr.equals(new String(postStr.getBytes("GB2312"), "GB2312"))) {
                        logger.debug(" This type is  GB2312" + postStr);
                        postStr = new String(postStr.getBytes("GB2312"), "UTF-8");
                        logger.debug(" change postStr to utf-8 " + postStr);
                    }
                    if (postStr.equals(new String(postStr.getBytes("GBK"), "GBK"))) {
                        logger.debug(" This type is  GBK");
                        postStr = new String(postStr.getBytes("GBK"), "UTF-8");

                    }
                    postStr = new String(postStr.getBytes("GB2312"), "UTF-8");*/
                    Document document;
                    try {
                        document = DocumentHelper.parseText(postStr);
                        Element root = document.getRootElement();
                        String fromUserName = root.elementText("FromUserName");
                        String toUserName = root.elementText("ToUserName");
                        String keyword = root.elementTextTrim("Content");
                        String msgType = root.elementTextTrim("MsgType");
                        String event = root.elementText("Event");
                        String eventKey = root.elementText("EventKey");
                        if (WechatConstant.MSG_TYPE_TEXT.equals(msgType)) {
                            responseStr = textResponseHandler(fromUserName, toUserName, keyword);
                        } else if (WechatConstant.MSG_TYPE_EVENT.equals(msgType)) {
                            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
                        } else {
                            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        logger.error("处理失败", e);
                        responseStr = "亲，网络超时，请稍后重试";
                    }
                }
            } else {
                responseStr = echostr;
            }
            logger.debug(">>>>>>>>>>>>>..responseStr>>>>>>>>>>>>>>>" + responseStr);
        } else {
            responseStr = "亲，非法访问，签名失败";
        }

        return new ResponseEntity<String>(responseStr, HttpStatus.OK);
    }

    /**
     * 鏂囨湰淇℃伅鍥炲
     *
     * @param fromUserName
     * @param toUserName
     * @param keyword
     * @return
     */
    private String textResponseHandler(String fromUserName, String toUserName,
                                       String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return WechatFactory
                    .formatText(toUserName, fromUserName, "未包含任何信息");
        } else {
            String responseStr = keyWordHandler(fromUserName, toUserName,
                    keyword);
            return WechatFactory
                    .formatText(toUserName, fromUserName, responseStr);
        }
    }

    private String keyWordHandler(String fromUserName, String toUserName,
                                  String keyword) {
        // TODO Auto-generated method stub
        String domain = WechatConstant.TOKEN;
        //String url = domain + "/IMSS/indexPage.do";
        return "HC小区物业管理系统是由java110团队于2017年4月份发起的前后端分离、分布式架构开源项目，目前我们的代码开源在github 和gitee上，开源项目由HC小区管理系统后端，HC小区管理系统前端，HC小区管理系统业主手机版和HC小区管理系统物业手机版，业务技术交流群：827669685";
    }

    /**
     * 事件处理
     *
     * @param fromUserName
     * @param toUserName
     * @param event
     * @param eventKey
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public String eventResponseHandler(String fromUserName, String toUserName, String keyWords, String event,
                                       String eventKey) throws Exception {
        String resultStr = "";
        // 璁㈤槄
        if (event.equals("subscribe")) {
            resultStr = "HC小区物业管理系统是由java110团队于2017年4月份发起的前后端分离、分布式架构开源项目，目前我们的代码开源在github 和gitee上，开源项目由HC小区管理系统后端，HC小区管理系统前端，HC小区管理系统业主手机版和HC小区管理系统物业手机版，业务技术交流群：827669685";
        } else if (event.equals("unsubscribe")) {

        } else if (event.equalsIgnoreCase("CLICK")) {
            resultStr = textResponseHandler(fromUserName, toUserName,
                    eventKey);
        } else {

        }
        return WechatFactory.formatText(toUserName, fromUserName, resultStr);
    }
}
