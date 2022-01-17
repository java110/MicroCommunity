/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.login.IOwnerAppLoginSMO;
import com.java110.api.smo.login.IWxLoginSMO;
import com.java110.api.smo.staff.IStaffAuthSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.WechatFactory;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class OwnerController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    private IOwnerAppLoginSMO ownerAppLoginSMOImpl;


    @Autowired
    private IStaffAuthSMO staffAuthSMOImpl;

    @Autowired
    private IWxLoginSMO wxLoginSMOImpl;

    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/loginOwner", method = RequestMethod.POST)
    public ResponseEntity<String> loginOwner(@RequestBody String postInfo, HttpServletRequest request) {
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }
        IPageData pd = PageData.newInstance().builder("", "", "", postInfo,
                "login", "", "", "", appId
        );
        ResponseEntity<String> responseEntity = ownerAppLoginSMOImpl.doLogin(pd);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject outParam = JSONObject.parseObject(responseEntity.getBody());
        pd.setToken(outParam.getString("token"));
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }


    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/loginOwnerByKey", method = RequestMethod.POST)
    public ResponseEntity<String> loginOwnerByKey(@RequestBody String postInfo, HttpServletRequest request) {
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }
        IPageData pd = PageData.newInstance().builder("", "", "", postInfo,
                "login", "", "", "", appId
        );
        ResponseEntity<String> responseEntity = ownerAppLoginSMOImpl.doLoginByKey(pd);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONObject outParam = JSONObject.parseObject(responseEntity.getBody());
        pd.setToken(outParam.getString("token"));
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }


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
        ResponseEntity responseEntity = ownerAppLoginSMOImpl.getPageAccessToken(pd, request);
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }

    /**
     * 微信公众号号鉴权
     *
     * @param request
     */
    @RequestMapping(path = "/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestParam String redirectUrl,
                                               @RequestParam String errorUrl,
                                               @RequestParam String loginFlag,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        IPageData pd = PageData.newInstance().builder("", "", "", "",
                "login", "", "", "", request.getHeader("app-id")
        );
        return ownerAppLoginSMOImpl.refreshToken(pd, redirectUrl, errorUrl, loginFlag, request, response);

    }

    /**
     * 微信登录接口
     *
     * @param request
     */
    @RequestMapping(path = "/openServiceNotifyOpenId")
    public ResponseEntity<String> openServiceNotifyOpenId(HttpServletRequest request) {

        Map<String, String> params = getParameterStringMap(request);
        String appId = params.get("appId");
        IPageData pd = PageData.newInstance().builder("", "", "", JSONObject.toJSONString(params),
                "login", "", "", "", appId
        );
        ResponseEntity responseEntity = ownerAppLoginSMOImpl.openServiceNotifyOpenId(pd, request);
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }

    /**
     * 微信公众号号鉴权
     *
     * @param request
     */
    @RequestMapping(path = "/refreshOpenId")
    public ResponseEntity<String> refreshOpenId(@RequestParam String redirectUrl,
                                                @RequestParam(required = false) String wAppId,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        IPageData pd = PageData.newInstance().builder("", "", "", "",
                "login", "", "", "", request.getHeader("app-id")
        );
        return ownerAppLoginSMOImpl.refreshOpenId(pd, redirectUrl,wAppId, request, response);

    }

    @RequestMapping(path = "/getWId")
    public ResponseEntity<String> getWId(@RequestParam String appId) {
        return ResultVo.createResponseEntity(WechatFactory.getWId(appId));
    }


    /**
     * 微信登录接口
     * /app/loginStaffWechatAuth
     *
     * @param request
     */
    @RequestMapping(path = "/loginStaffWechatAuth")
    public ResponseEntity<String> loginStaffWechatAuth(HttpServletRequest request) {

        Map<String, String> params = getParameterStringMap(request);
        String appId = params.get("appId");
        IPageData pd = PageData.newInstance().builder("", "", "", JSONObject.toJSONString(params),
                "login", "", "", "", appId
        );
        ResponseEntity responseEntity = staffAuthSMOImpl.getPageAccessToken(pd, request);
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, pd);
        return responseEntity;
    }

    /**
     * 微信公众号号鉴权
     * /app/staffAuth
     *
     * @param request
     */
    @RequestMapping(path = "/staffAuth")
    public ResponseEntity<String> staffAuth(@RequestParam(value = "communityId") String communityId,
                                            @RequestParam(value = "staffId") String staffId,
                                            @RequestParam(value = "storeId") String storeId,
                                            @RequestParam(value = "appId") String appId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        IPageData pd = PageData.newInstance().builder("", "", "", "",
                "login", "", "", "", appId);
        return staffAuthSMOImpl.refreshToken(pd, communityId, staffId, storeId, request, response);

    }

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

        String appId = request.getHeader("APP_ID");
        if (StringUtil.isEmpty(appId)) {
            appId = request.getHeader("APP-ID");
        }
        IPageData pd = PageData.newInstance().builder("", "", "", postInfo,
                "", "", "", "",
                appId);

        return wxLoginSMOImpl.doLogin(pd);
    }


    @RequestMapping(path = "/getWxPhoto", method = RequestMethod.POST)
    public ResponseEntity<String> getWxPhoto(@RequestBody String postInfo) {
        JSONObject postObj = JSONObject.parseObject(postInfo);


        String photoInfo = WechatFactory.getPhoneNumberBeanS5(postObj.getString("decryptData"),
                postObj.getString("key"), postObj.getString("iv"));
        JSONObject photoObj = JSONObject.parseObject(photoInfo);
        CommonCache.setValue(postObj.getString("key"), photoObj.getString("phoneNumber"), CommonCache.defaultExpireTime);
        return ResultVo.createResponseEntity(photoObj);

    }

}
