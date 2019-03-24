package com.java110.web.components;

import com.java110.core.context.IPageData;
import com.java110.web.smo.IRegisterServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 用户注册事件
 * Created by wuxw on 2019/3/23.
 */
@Component("register")
public class RegisterComponent {


    @Autowired
    IRegisterServiceSMO registerServiceSMOImpl;


    /**
     * 用户注册
     * @param pd 页面封装数据
     * @return
     */
    public ResponseEntity<String> doRegister(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity =  registerServiceSMOImpl.doRegister(pd);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }


    public IRegisterServiceSMO getRegisterServiceSMOImpl() {
        return registerServiceSMOImpl;
    }

    public void setRegisterServiceSMOImpl(IRegisterServiceSMO registerServiceSMOImpl) {
        this.registerServiceSMOImpl = registerServiceSMOImpl;
    }
}
