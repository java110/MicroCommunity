package com.java110.feign.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * 服务测试
 *
 * ribbon
 *
 * feign
 *
 *
 * Created by wuxw on 2017/4/5.
 */
@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestFeignHystrixClient testFeignHystrixClient;

    @GetMapping("/test/{id}")
    public String sayHello(@PathVariable Long id) {
        return restTemplate.getForObject("http://user-service/test/sayHello?param="+id,String.class);
    }

    @GetMapping("/testFeign/{id}")
    public String sayHelloFeign(@PathVariable Long id){
        return testFeignHystrixClient.sayHello(""+id);
    }
}
