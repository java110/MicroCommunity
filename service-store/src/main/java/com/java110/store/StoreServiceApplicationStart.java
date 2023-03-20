/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.store;

import com.java110.core.annotation.Java110CmdDiscovery;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
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
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.store", "com.java110.core",
        "com.java110.config.properties.code", "com.java110.db","com.java110.doc"})
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.store.listener"})
@Java110CmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.java110.store.cmd"})
@EnableFeignClients(basePackages = {
        "com.java110.intf.community",
        "com.java110.intf.fee",
        "com.java110.intf.user",
        "com.java110.intf.common",
        "com.java110.intf.acct",
        "com.java110.intf.order",
        "com.java110.intf.mall"
})
public class StoreServiceApplicationStart {

    private final static Logger logger = LoggerFactory.getLogger(StoreServiceApplicationStart.class);


    private final static String LISTENER_PATH = "java110.StoreService.listeners";

    @Resource
    private Java110RestTemplateInterceptor java110RestTemplateInterceptor;
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
    public RestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        return restTemplate;
    }


    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();

            ApplicationContext context = SpringApplication.run(StoreServiceApplicationStart.class, args);
            ServiceStartInit.initSystemConfig(context);
            //加载业务侦听
            //SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);
            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }
}