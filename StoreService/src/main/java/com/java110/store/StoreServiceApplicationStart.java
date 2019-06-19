package com.java110.store;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.event.service.init.SystemStartLoadBusinessConfigure;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
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
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.store", "com.java110.core", "com.java110.cache", "com.java110.db"})
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.store.listener"})
@EnableFeignClients(basePackages = {"com.java110.core.smo.community",
        "com.java110.core.smo.fee",
        "com.java110.core.smo.floor",
        "com.java110.core.smo.owner",
        "com.java110.core.smo.parkingSpace",
        "com.java110.core.smo.room",
        "com.java110.core.smo.unit",
        "com.java110.core.smo.user",
})
public class StoreServiceApplicationStart {

    private final static Logger logger = LoggerFactory.getLogger(StoreServiceApplicationStart.class);


    private final static String LISTENER_PATH = "java110.StoreService.listeners";

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        return restTemplate;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(StoreServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);
        //加载业务侦听
        //SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);
    }
}