package com.java110.center;

import com.java110.center.smo.ICenterServiceCacheSMO;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.event.center.init.EventConfigInit;
import com.java110.service.init.ServiceStartInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages={"com.java110.service","com.java110.center","com.java110.core","com.java110.event.center","com.java110.cache"})
@EnableDiscoveryClient
//@EnableConfigurationProperties(EventProperties.class)
public class CenterServiceApplicationStart {

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        return restTemplate;
    }

    public static void main(String[] args) throws Exception{
        ApplicationContext context = SpringApplication.run(CenterServiceApplicationStart.class, args);

        //服务启动加载
        ServiceStartInit.initSystemConfig(context);

        //加载事件数据
        EventConfigInit.initSystemConfig();

        //刷新缓存
        flushMainCache(args);
    }


    /**
     * 刷新主要的缓存
     * @param args
     */
    private static void flushMainCache(String []args) {
        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-Dcache")) {
                ICenterServiceCacheSMO centerServiceCacheSMO = (ICenterServiceCacheSMO) ApplicationContextFactory.getBean("centerServiceCacheSMOImpl");
                centerServiceCacheSMO.startFlush();
            }
        }
    }
}