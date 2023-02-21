package com.java110.user;

import com.java110.core.annotation.Java110CmdDiscovery;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.client.OutRestTemplate;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.doc.annotation.Java110CmdDocDiscovery;
import com.java110.doc.registrar.ApiDocCmdPublishing;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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

import javax.annotation.Resource;
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
@SpringBootApplication(
        scanBasePackages = {"com.java110.service","com.java110.core", "com.java110.user",  "com.java110.config.properties.code", "com.java110.db","com.java110.doc"},
        excludeName = {"com.java110.intf.user"}
)
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.user.listener"})
@Java110CmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.java110.user.cmd"})
@EnableFeignClients(basePackages = {"com.java110.intf.community","com.java110.intf.common","com.java110.intf.store","com.java110.intf.oa",
        "com.java110.intf.fee","com.java110.intf.order","com.java110.intf.mall","com.java110.intf.report","com.java110.intf.acct"})

// 文档
@Java110CmdDocDiscovery(basePackages = {"com.java110.user.cmd"},cmdDocClass = ApiDocCmdPublishing.class)
public class UserServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(UserServiceApplicationStart.class);

    private static final String LISTENER_PATH = "java110.UserService.listeners";

    @Resource
    private Java110RestTemplateInterceptor java110RestTemplateInterceptor;

    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(UserServiceApplicationStart.class, args);
            ServiceStartInit.initSystemConfig(context);
            //加载业务侦听
            // SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);
            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
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
        restTemplate.getInterceptors().add(java110RestTemplateInterceptor);
        return restTemplate;
    }

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
}