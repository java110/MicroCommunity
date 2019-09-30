package com.java110.web.components;


import com.java110.core.context.IPageData;
import com.java110.web.smo.ILoginServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component("login")
public class LoginComponent {



    @Autowired
    private ILoginServiceSMO loginServiceSMOImpl;
    /**
     * 用户登录
     * @param pd
     * @return
     */
    public ResponseEntity<String> doLogin(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity =  loginServiceSMOImpl.doLogin(pd);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }



    public ILoginServiceSMO getLoginServiceSMOImpl() {
        return loginServiceSMOImpl;
    }

    public void setLoginServiceSMOImpl(ILoginServiceSMO loginServiceSMOImpl) {
        this.loginServiceSMOImpl = loginServiceSMOImpl;
    }
}
