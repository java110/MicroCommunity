package com.java110.front.controller.wechat;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.front.smo.wechatGateway.IWechatGatewaySMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private IWechatGatewaySMO wechatGatewaySMOImpl;


    /**
     * 微信登录接口
     *
     * @param request
     */
    @RequestMapping(path = "/gateway", method = RequestMethod.GET)
    public ResponseEntity<String> gateway(HttpServletRequest request) {

        String token = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.TOKEN);
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String communityId = request.getParameter("communityId");
        String java110AppId = request.getParameter("java110AppId");
        IPageData pd = PageData.newInstance().builder("-1", "", "", "",
                "", "", "", "",
                java110AppId);
        if(!StringUtil.isEmpty(communityId)){
            SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
            smallWeChatDto.setObjId(communityId);
            smallWeChatDto = wechatGatewaySMOImpl.getSmallWechat(pd,smallWeChatDto);

            if(smallWeChatDto !=null){
                List<SmallWechatAttrDto> smallWechatAttrDtos = smallWeChatDto.getSmallWechatAttrs();

                if(smallWechatAttrDtos != null){
                   // for()
                }
            }
        }
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
        try {
            if (signature1.equals(signature)) {
                responseStr = echostr;
                logger.debug(">>>>>>>>>>>>>..responseStr>>>>>>>>>>>>>>>" + responseStr);
            } else {
                responseStr = "亲，非法访问，签名失败";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("处理失败", e);
            responseStr = "亲，网络超时，请稍后重试";
        }

        return new ResponseEntity<String>(responseStr, HttpStatus.OK);
    }

    /**
     * 微信登录接口
     *
     * @param request
     */
    @RequestMapping(path = "/gateway", method = RequestMethod.POST)
    public ResponseEntity<String> gateway(@RequestBody String param, HttpServletRequest request) {

        String token = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.TOKEN);
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String openId = request.getParameter("openid");
        String java110AppId = request.getParameter("java110AppId");
        String responseStr = "";
        ResponseEntity<String> responseEntity = null;
        logger.debug("token = " + token + "||||" + "signature = " + signature + "|||" + "timestamp = "
                + timestamp + "|||" + "nonce = " + nonce + "|||| param = " + param + "|||| openId= " + openId);
        String sourceString = "";
        String[] ss = new String[]{token, timestamp, nonce};
        Arrays.sort(ss);
        for (String s : ss) {
            sourceString += s;
        }
        String signature1 = AuthenticationFactory.SHA1Encode(sourceString).toLowerCase();
        logger.debug("sourceString = " + sourceString + "||||" + "signature1 = " + signature1);
        try {
            if (!signature1.equals(signature)) {
                responseStr = "亲，非法访问，签名失败";
                return new ResponseEntity<String>(responseStr, HttpStatus.OK);
            }
            String postStr = param;
            if (StringUtil.isEmpty(postStr)) {
                responseStr = "未输入任何内容";
                return new ResponseEntity<String>(responseStr, HttpStatus.OK);
            }
            Document document = DocumentHelper.parseText(postStr);
            Element root = document.getRootElement();
            String fromUserName = root.elementText("FromUserName");
            String toUserName = root.elementText("ToUserName");
            String keyword = root.elementTextTrim("Content");
            String msgType = root.elementTextTrim("MsgType");
            String event = root.elementText("Event");
            String eventKey = root.elementText("EventKey");
            JSONObject paramIn = new JSONObject();
            paramIn.put("fromUserName", fromUserName);
            paramIn.put("toUserName", toUserName);
            paramIn.put("keyword", keyword);
            paramIn.put("msgType", msgType);
            paramIn.put("event", event);
            paramIn.put("eventKey", eventKey);
            IPageData pd = PageData.newInstance().builder("-1", "", "", paramIn.toJSONString(),
                    "", "", "", "",
                    java110AppId);
            responseEntity = wechatGatewaySMOImpl.gateway(pd);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("处理失败", e);
            responseStr = "亲，网络超时，请稍后重试";
            responseEntity = new ResponseEntity<String>(responseStr, HttpStatus.OK);
        }

        return responseEntity;
    }
}
