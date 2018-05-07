package com.java110.console.controller;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录 控制类
 * Created by wuxw on 2018/5/2.
 */
@Controller
public class LoginController extends BaseController {



    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        return "login";
    }


}
