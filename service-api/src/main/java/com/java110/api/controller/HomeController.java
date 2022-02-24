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
package com.java110.api.controller;

import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IFlowServiceSMO;
import com.java110.utils.constant.CommonConstant;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
    private IFlowServiceSMO flowServiceSMOImpl;

    @RequestMapping(path = "/")
    public String index(Model model, HttpServletRequest request) {
        String template = "index";

        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);

        if (!flowServiceSMOImpl.hasStoreInfos(pd)) {
            //初始化 商户信息
            template = "init_company";
        }

        return template;
    }


    public IFlowServiceSMO getFlowServiceSMOImpl() {
        return flowServiceSMOImpl;
    }

    public void setFlowServiceSMOImpl(IFlowServiceSMO flowServiceSMOImpl) {
        this.flowServiceSMOImpl = flowServiceSMOImpl;
    }
}
