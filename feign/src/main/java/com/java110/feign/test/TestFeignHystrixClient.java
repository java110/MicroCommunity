package com.java110.feign.test;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 使用@FeignClient注解的fallback属性，指定fallback类
 * @author eacdy
 */
@FeignClient(name = "user-service", fallback = HystrixClientFallback.class)
public interface TestFeignHystrixClient {

  @RequestMapping("/test/sayHello")
  public String sayHello(@RequestParam("param") String param);

}
