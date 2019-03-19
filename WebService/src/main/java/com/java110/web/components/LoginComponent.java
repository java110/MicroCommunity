package com.java110.web.components;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("login")
public class LoginComponent {

    /**
     * 用户登录
     * @param userInfo
     * @return
     */
    public ResponseEntity<String> doLogin(String userInfo){

        ResponseEntity<String> responseEntity = null;

        try{
            responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }

    private void userLogin(){


    }
}
