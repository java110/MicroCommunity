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
import com.java110.service.api.BusinessApi;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误页面
 * Created by wuxw on 2018/5/2.
 */
@Controller
public class SystemErrorController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BusinessApi.class);


    @RequestMapping(path = "/system/error")
    public String error(Model model, HttpServletRequest request) {
        String code = request.getParameter("code");
        String msg = request.getParameter("msg");
        if(StringUtil.isNullOrNone(code) || StringUtil.isNullOrNone(msg)){
            code = ResponseConstant.RESULT_CODE_INNER_ERROR;
            msg = "系统内部异常";
        }
        model.addAttribute("code",code);
        model.addAttribute("msg",msg);
        //3.0 查询各个系统调用量
        return "error";
    }
}
