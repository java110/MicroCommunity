package com.java110.entity.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试服务
 * Created by wuxw on 2017/4/5.
 */
@RestController
public class TestServiceSMOImpl {

    @RequestMapping("/test/sayHello")
    public String sayHello(@RequestParam String param){
        return param+",hello";
    }
}
