package com.java110.api.components;


import com.java110.core.context.IPageData;
import com.java110.api.smo.login.ILoginServiceSMO;
import com.java110.api.smo.sys.ISysServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("login")
public class LoginComponent {


    @Autowired
    private ILoginServiceSMO loginServiceSMOImpl;

    @Autowired
    private ISysServiceSMO sysServiceSMOImpl;

    /**
     * 用户登录
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> doLogin(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
                responseEntity = loginServiceSMOImpl.doLogin(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
        
    }

    public ResponseEntity<String> getSysInfo(IPageData pd) {
        return sysServiceSMOImpl.getSysInfo(pd);
    }


    public ILoginServiceSMO getLoginServiceSMOImpl() {
        return loginServiceSMOImpl;
    }

    public void setLoginServiceSMOImpl(ILoginServiceSMO loginServiceSMOImpl) {
        this.loginServiceSMOImpl = loginServiceSMOImpl;
    }

    public ISysServiceSMO getSysServiceSMOImpl() {
        return sysServiceSMOImpl;
    }

    public void setSysServiceSMOImpl(ISysServiceSMO sysServiceSMOImpl) {
        this.sysServiceSMOImpl = sysServiceSMOImpl;
    }
}
