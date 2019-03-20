package com.java110.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.web.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制中心处理类
 * Created by wuxw on 2018/4/25.
 */
@Controller
public class HomeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private IConsoleServiceSMO consoleServiceSMOImpl;

    @RequestMapping(path = "/")
    public String index(Model model, HttpServletRequest request) {
        String template = "index";

            return template;
    }



    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
