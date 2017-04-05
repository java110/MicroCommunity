package com.java110.feign.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 这边采取了和Spring Cloud官方文档相同的做法，将fallback类作为内部类放入Feign的接口中，当然也可以单独写一个fallback类。
 * @author eacdy
 */

@Component
public class HystrixClientFallback implements TestFeignHystrixClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixClientFallback.class);

    /**
     * hystrix fallback方法
     * @param param id
     * @return 默认的用户
     */
    @Override
    public String sayHello(@RequestParam String param) {
        HystrixClientFallback.LOGGER.info("异常发生，进入fallback方法，接收的参数：id = {}", param);

        return "异常了";
    }
}