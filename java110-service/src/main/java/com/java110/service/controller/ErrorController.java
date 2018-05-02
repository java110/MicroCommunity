package com.java110.service.controller;

import com.java110.common.exception.NoAuthorityException;
import com.java110.common.exception.SMOException;
import com.java110.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 错误页面
 * Created by wuxw on 2018/5/2.
 */
@Controller
public class ErrorController extends BaseController {

    @RequestMapping(path = "/error")
    public String error(Model model,@RequestParam("code") String code,@RequestParam("msg") String msg) {
            model.addAttribute("code",code);
            model.addAttribute("msg",msg);
            //3.0 查询各个系统调用量
            return "error";
    }
}
