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
package com.java110.front.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.front.smo.staff.IStaffAuthSMO;
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
public class StaffWechatAuthController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(StaffWechatAuthController.class);

    @Autowired
    private IStaffAuthSMO staffAuthSMOImpl;


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

}
