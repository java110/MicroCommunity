package com.java110.web.components;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component(value="vue_test")
public class VueTestComponent {


    /**
     * 测试版本号
     * @return
     */
    public ResponseEntity<String> getTestVersion(String msg){

        return new ResponseEntity<String>(msg+ " vue test v0.0.1", HttpStatus.OK);
    }

}
