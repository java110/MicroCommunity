package com.java110.front.controller.mina;

import com.alibaba.fastjson.JSONObject;
import com.java110.front.smo.wxLogin.IWxLoginSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.utils.util.StringUtil;
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
 * 微信小程序登录处理类
 */
@RestController
@RequestMapping(path = "/app")
public class WxLoginController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(WxLoginController.class);

    @Autowired
    private IWxLoginSMO wxLoginSMOImpl;


    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/loginWx", method = RequestMethod.POST)
    public ResponseEntity<String> loginWx(@RequestBody String postInfo, HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        JSONObject postObj = JSONObject.parseObject(postInfo);
        String code = JSONObject.parseObject(postInfo).getString("code");
        JSONObject userInfo = postObj.getJSONObject("userInfo");
        if (code == null || userInfo == null) {
            logger.error("code is null");
            responseEntity = new ResponseEntity<>("code is null", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if(StringUtil.isEmpty(appId)){
            appId = request.getHeader("APP-ID");
        }
        IPageData pd = PageData.newInstance().builder("", "","", postInfo,
                "", "", "", "",
                appId);

        return wxLoginSMOImpl.doLogin(pd);
    }

}
