package com.java110;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication
@EnableAutoConfiguration
public class AppSpringBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(AppSpringBootApplication.class, args);
    }
}