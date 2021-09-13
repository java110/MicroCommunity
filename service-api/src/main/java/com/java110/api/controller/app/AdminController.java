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

import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.login.IAdminLoginPropertyAccountServiceSMO;
import com.java110.utils.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员免密方式下登录至物业管理员账号下操作物业相关数据
 * <p>
 * 具体流程为 管理员需要填写当前账号密码
 * 如果传入账号密码正确 则登录至 相应物业账号下
 * add by 吴学文 2021/1/3
 **/
@RestController
@RequestMapping(path = "/app")
public class AdminController {

    @Autowired
    private IAdminLoginPropertyAccountServiceSMO adminLoginPropertyAccountServiceSMOImpl;

    /**
     * 管理员免密登录至 执行的物业账户下
     *
     * @param request
     */
    @RequestMapping(path = "/adminLoginPropertyAccount", method = RequestMethod.POST)
    public ResponseEntity<String> adminLoginPropertyAccount(HttpServletRequest request) {
        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(), pd.getReqData(),
                "login", "", "", pd.getSessionId(),
                pd.getAppId(),
                pd.getHeaders());
        ResponseEntity<String> responseEntity = adminLoginPropertyAccountServiceSMOImpl.doLogin(newPd);
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA, newPd);
        return responseEntity;
    }
}
