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
package com.java110.api;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.trace.Java110FeignClientInterceptor;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.core.log.LoggerFactory;
import com.java110.doc.annotation.Java110ApiDocDiscovery;
import com.java110.doc.registrar.ApiDocPublishing;
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
 * spring boot 初始化启动类
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
        "com.java110.api",
        "com.java110.core",
        "com.java110.config.properties.code",
        "com.java110.doc",
})
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = ServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.api.listener"})
@EnableSwagger2
//@EnableConfigurationProperties(EventProperties.class)
@EnableFeignClients(basePackages = {
        "com.java110.intf.acct",
        "com.java110.intf.code",
        "com.java110.intf.common",
        "com.java110.intf.community",
        "com.java110.intf.demo",
        "com.java110.intf.dev",
        "com.java110.intf.fee",
        "com.java110.intf.goods",
        "com.java110.intf.job",
        "com.java110.intf.oa",
        "com.java110.intf.order",
        "com.java110.intf.report",
        "com.java110.intf.store",
        "com.java110.intf.user"
})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync

//文档
@Java110ApiDocDiscovery(basePackages = {"com.java110.api.rest"},apiDocClass = ApiDocPublishing.class)
public class ApiApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(ApiApplicationStart.class);

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

    /**
     * swagger 插件
     *
     * @return Docket 对象
     */
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
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

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HC小区管理系统 APIs")
                .description("HC小区管理系统提供的所有能力")
                .termsOfServiceUrl("https://github.com/java110/MicroCommunity")
                .contact("吴学文")
                .version("1.0")
                .build();
    }


    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(ApiApplicationStart.class, args);
            //服务启动加载
            ServiceStartInit.initSystemConfig(context);
            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }

}