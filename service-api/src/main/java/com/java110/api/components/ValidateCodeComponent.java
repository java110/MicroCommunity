package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.login.ILoginServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 验证码组件
 * Created by wuxw on 2019/3/20.
 */
@Component("validate-code")
public class ValidateCodeComponent {

    @Autowired
    private ILoginServiceSMO loginServiceSMOImpl;

    /**
     * 生成 验证码
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> generateValidateCode(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = loginServiceSMOImpl.generateValidateCode(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        
        return responseEntity;
    }

    /**
     * 校验验证码
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> validate(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = loginServiceSMOImpl.validate(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        
        return responseEntity;
    }


    public ILoginServiceSMO getLoginServiceSMOImpl() {
        return loginServiceSMOImpl;
    }

    public void setLoginServiceSMOImpl(ILoginServiceSMO loginServiceSMOImpl) {
        this.loginServiceSMOImpl = loginServiceSMOImpl;
    }
}
