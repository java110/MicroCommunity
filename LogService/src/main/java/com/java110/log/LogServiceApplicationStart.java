package com.java110.log;

/**
 * Hello world!
 */

import com.java110.service.init.ServiceStartInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * 日志服务初始化启动类 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {"com.java110.service","com.java110.log",
"com.java110.feign.code","com.java110.core","com.java110.cache"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.java110.core.smo"})
public class LogServiceApplicationStart {


    public static void main(String[] args) throws Exception {

        ApplicationContext context =  SpringApplication.run(LogServiceApplicationStart.class, args);

        ServiceStartInit.initSystemConfig(context);

    }
}
