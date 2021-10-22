package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IRegisterServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 手机验证码校验组件
 * Created by wuxw on 2019/3/23.
 */

@Component("validate-tel")
public class ValidateTelComponent {

    @Autowired
    IRegisterServiceSMO registerServiceSMOImpl;

    /**
     * 手机发送验证码
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> sendTelMessageCode(IPageData pd) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = registerServiceSMOImpl.sendTelMessageCode(pd);
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
            responseEntity = registerServiceSMOImpl.validate(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }


    public IRegisterServiceSMO getRegisterServiceSMOImpl() {
        return registerServiceSMOImpl;
    }

    public void setRegisterServiceSMOImpl(IRegisterServiceSMO registerServiceSMOImpl) {
        this.registerServiceSMOImpl = registerServiceSMOImpl;
    }
}
