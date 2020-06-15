package com.java110.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.front.smo.ownerLogin.IOwnerAppLoginSMO;
import com.java110.utils.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信小程序登录处理类
 */
@RestController
@RequestMapping(path = "/app")
public class LoginOwnerWechatAuthController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(LoginOwnerWechatAuthController.class);

    @Autowired
    private IOwnerAppLoginSMO ownerAppLoginSMOImpl;


    /**
     * 微信登录接口
     *
     * @param request
     */
    @RequestMapping(path = "/loginOwnerWechatAuth")
    public ResponseEntity<String> loginOwnerWechatAuth(HttpServletRequest request) {

        Map<String, String> params = getParameterStringMap(request);
        String appId = params.get("appId");
        IPageData pd = PageData.newInstance().builder("", "", "", JSONObject.toJSONString(params),
                "login", "", "", "", appId
        );
        ResponseEntity responseEntity = ownerAppLoginSMOImpl.getPageAccessToken(pd);
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }

    /**
     * 微信公众号号鉴权
     *
     * @param request
     */
    @RequestMapping(path = "/refreshToken")
    public String refreshToken(@RequestParam String redirectUrl,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        return ownerAppLoginSMOImpl.refreshToken(null, redirectUrl, request, response);

    }

}
