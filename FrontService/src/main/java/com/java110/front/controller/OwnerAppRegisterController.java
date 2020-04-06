package com.java110.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.front.smo.common.ICommonGetSMO;
import com.java110.front.smo.ownerLogin.IOwnerAppLoginSMO;
import com.java110.front.smo.wxLogin.IWxLoginSMO;
import com.java110.utils.constant.CommonConstant;
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
public class OwnerAppRegisterController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(OwnerAppRegisterController.class);

    @Autowired
    private IOwnerAppLoginSMO ownerAppLoginSMOImpl;



    /**
     * 注册业主接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/registerOwner", method = RequestMethod.POST)
    public ResponseEntity<String> loginOwner(@RequestBody String postInfo, HttpServletRequest request) {
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        IPageData pd = PageData.newInstance().builder("", "", "", postInfo,
                "login", "", "", "",
                request.getHeader("APP_ID"));
        ResponseEntity<String> responseEntity = ownerAppLoginSMOImpl.doLogin(pd);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        JSONObject outParam = JSONObject.parseObject(responseEntity.getBody());
        pd.setToken(outParam.getString("token"));
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA,pd);
        return responseEntity;
    }

}
