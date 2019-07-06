package com.java110.rule;

import com.java110.config.properties.EventProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = "com.java110.service,com.java110.rule")
@EnableDiscoveryClient
@EnableConfigurationProperties(EventProperties.class)
@EnableFeignClients(basePackages = {"com.java110.core.smo"})
public class AppSpringBootApplication {

    private final static Logger logger = LoggerFactory.getLogger(AppSpringBootApplication.class);


    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppSpringBootApplication.class, args);
    }
}