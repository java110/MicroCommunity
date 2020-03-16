package com.java110.front.controller;

import com.java110.core.base.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName HealthController
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/16 23:57
 * @Version 1.0
 * add by wuxw 2020/3/16
 **/
@RestController
public class TokenController extends BaseController {

    @RequestMapping(path = "/token/checkToken")
    public ResponseEntity<String> checkToken(HttpServletRequest request) {
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

}
