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
package com.java110.boot;

import com.java110.core.annotation.Java110CmdDiscovery;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.core.log.LoggerFactory;
import com.java110.core.trace.Java110FeignClientInterceptor;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.service.init.ServiceStartInit;
import io.swagger.annotations.ApiOperation;
import okhttp3.ConnectionPool;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/**
 * 这个服务是将 系统部署为spring boot版
 * 如果是spring cloud 微服务部署 不用启动这个类
 *
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {
        "com.java110.service.configuration",
        "com.java110.service.init",
        "com.java110.core",
        "com.java110.config.properties.code",
        "com.java110.acct",
        "com.java110.common",
        "com.java110.community",
        "com.java110.dev",
        "com.java110.fee",
        "com.java110.job",
        "com.java110.oa",
        "com.java110.order",
        "com.java110.report",
        "com.java110.store",
        "com.java110.user",
})
@Java110CmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {
                "com.java110.acct.cmd",
                "com.java110.common.cmd",
                "com.java110.community.cmd",
                "com.java110.dev.cmd",
                "com.java110.fee.cmd",
                "com.java110.job.cmd",
                "com.java110.oa.cmd",
                "com.java110.order.cmd",
                "com.java110.report.cmd",
                "com.java110.store.cmd",
                "com.java110.user.cmd"
        })
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync
public class BootApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(BootApplicationStart.class);

    @Resource
    private Java110RestTemplateInterceptor java110RestTemplateInterceptor;

    /**
     * 实例化RestTemplate
     *
     * @return restTemplate
     */
    @Bean
    public RestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        return restTemplate;
    }

    @Bean
    @ConditionalOnBean(Java110FeignClientInterceptor.class)
    public okhttp3.OkHttpClient okHttpClient(@Autowired
                                                     Java110FeignClientInterceptor okHttpLoggingInterceptor){
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS) //读取超时
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
                .addInterceptor(okHttpLoggingInterceptor);
        return ClientBuilder.build();
    }

    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(BootApplicationStart.class, args);
            //服务启动加载
            ServiceStartInit.initSystemConfig(context);
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }

}