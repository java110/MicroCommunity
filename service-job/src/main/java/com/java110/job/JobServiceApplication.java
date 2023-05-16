package com.java110.job;

import com.java110.core.annotation.Java110CmdDiscovery;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.core.log.LoggerFactory;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootApplication(
        scanBasePackages = {"com.java110.service",
                "com.java110.job",
                "com.java110.core",
                "com.java110.config.properties.code",
                "com.java110.db",
                "com.java110.doc"},
        excludeName = {"com.java110.intf.jobservice"}
)
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.job.listener"})
@Java110CmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.java110.job.cmd"})
@EnableFeignClients(basePackages = {
        "com.java110.intf.community",
        "com.java110.intf.common",
        "com.java110.intf.fee",
        "com.java110.intf.user",
        "com.java110.intf.order",
        "com.java110.intf.store",
        "com.java110.intf.report",
        "com.java110.intf.acct",
        "com.java110.intf.oa",
        "com.java110.intf.dev",
        "com.java110.intf.goods"
})
@EnableScheduling
@EnableAsync
public class JobServiceApplication {
    private static Logger logger = LoggerFactory.getLogger(JobServiceApplication.class);

    private static final String LISTENER_PATH = "java110.job.listeners";

    @Resource
    private Java110RestTemplateInterceptor java110RestTemplateInterceptor;

    @Bean
    public OutRestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        OutRestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(OutRestTemplate.class);
        restTemplate.getInterceptors().add(java110RestTemplateInterceptor);

        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(5000);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    @Bean
    //@LoadBalanced
    public RestTemplate formRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        return restTemplate;
    }

    public static void main(String[] args) throws Exception {
        ServiceStartInit.preInitSystemConfig();
        ApplicationContext context = SpringApplication.run(JobServiceApplication.class, args);
        ServiceStartInit.initSystemConfig(context);
        //加载业务侦听
        // SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();


    }
}
