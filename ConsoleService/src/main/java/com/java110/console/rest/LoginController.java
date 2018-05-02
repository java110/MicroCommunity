package com.java110.console.rest;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录 控制类
 * Created by wuxw on 2018/5/2.
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    private IConsoleServiceSMO consoleServiceSMOImpl;

    /**
     * 登录
     * 协议：
     * {
     "meta":{
     "method":"",//主要用于，日志记录
     "requestTime":"",
     "transactionId":"请求流水" //不需要填 有系统统一设置
     },
     "param":{
     *     "userCode":"",
     *     "userPwd":"",
     *     "code":"",
     * }
     }
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        PageData pd = null;
        try{
            pd = this.getPageData(request);


        }catch (Exception e){
            return DataTransactionFactory.pageResponseJson(ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
        }

        return pd.getResJson().toJSONString();
    }

    public IConsoleServiceSMO getConsoleServiceSMOImpl() {
        return consoleServiceSMOImpl;
    }

    public void setConsoleServiceSMOImpl(IConsoleServiceSMO consoleServiceSMOImpl) {
        this.consoleServiceSMOImpl = consoleServiceSMOImpl;
    }
}
