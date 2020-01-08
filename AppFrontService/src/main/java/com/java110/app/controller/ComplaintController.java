package com.java110.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.wxLogin.IWxLoginSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
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

/**
 * 投诉意见
 */
@RestController
@RequestMapping(path = "/app")
public class ComplaintController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(ComplaintController.class);

    @Autowired
    private IWxLoginSMO wxLoginSMOImpl;


    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/complaint", method = RequestMethod.POST)
    public ResponseEntity<String> complaint(@RequestBody String postInfo, HttpServletRequest request) {

        return wxLoginSMOImpl.doLogin(null);
    }

}
