package com.java110.app.controller;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序登录处理类
 */
@RestController
public class WxLogin  extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(WxLogin.class);


    @RequestMapping(path = "/login")
    public String login(Model model, HttpServletRequest request) {
        String template = "index";

        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

        return template;
    }

}
