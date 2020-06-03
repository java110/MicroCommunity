package com.java110.job;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.event.center.DataFlowEventPublishing;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@SpringBootApplication(
        scanBasePackages = {"com.java110.service",
                "com.java110.job",
                "com.java110.core",
                "com.java110.config.properties.code",
                "com.java110.db"},
        excludeName = {"com.java110.core.smo.jobservice"}
)
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.job.listener"})
@EnableFeignClients(basePackages = {"com.java110.core.smo.community",
        "com.java110.core.smo.fee",
        "com.java110.core.smo.floor",
        "com.java110.core.smo.owner",
        "com.java110.core.smo.parkingSpace",
        "com.java110.core.smo.room",
        "com.java110.core.smo.unit",
        "com.java110.core.smo.user",
})
@EnableScheduling
public class JobServiceApplication {
    private static Logger logger = LoggerFactory.getLogger(JobServiceApplication.class);

    private static final String LISTENER_PATH = "java110.job.listeners";

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
        ApplicationContext context = SpringApplication.run(JobServiceApplication.class, args);
        ServiceStartInit.initSystemConfig(context);
        //加载业务侦听
        // SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);
    }
}
