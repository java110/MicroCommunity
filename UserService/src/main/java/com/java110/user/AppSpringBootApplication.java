package com.java110.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages="com.java110.service,com.java110.user")
@EnableDiscoveryClient
public class AppSpringBootApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(AppSpringBootApplication.class, args);
    }
}