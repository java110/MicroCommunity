package com.java110.console.rest;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.SMOException;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.console.smo.IConsoleServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wuxw on 2018/5/7.
 */
@RestController
public class LoginRest extends BaseController {


    @Autowired
    private IConsoleServiceSMO consoleServiceSMOImpl;
    /**
     * 登录
     * 协议：
     * {"meta":{"method":"login","requestTime":"20180506013211"},"param":{"userCode":"123","userPwd":"123","pageSign":""}}}
     }
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/loginRest/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request) {
        PageData pd = null;
        try {
            pd = this.getPageData(request);
            consoleServiceSMOImpl.login(pd);
        }catch (IllegalArgumentException e){
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_PARAM_ERROR,e.getMessage(),null);
        }catch (SMOException e){
            return DataTransactionFactory.pageResponseJson(pd,e.getResult().getCode(),e.getMessage(),null);
        }catch (Exception e){
            logger.error("异常信息：",e);
            return DataTransactionFactory.pageResponseJson(pd,ResponseConstant.RESULT_CODE_ERROR,"请求参数出错 ",null);
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
