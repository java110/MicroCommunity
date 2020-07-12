
package com.java110.api;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.service.init.ServiceStartInit;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@SpringBootApplication(scanBasePackages = {
        "com.java110.service.configuration",
        "com.java110.service.init",
        "com.java110.api",
        "com.java110.core",
        "com.java110.config.properties.code",
        })
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = ServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.api.listener"})
@EnableSwagger2
//@EnableConfigurationProperties(EventProperties.class)
@EnableFeignClients(basePackages = {"com.java110.intf"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ApiApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(ApiApplicationStart.class);

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
        try{
            ApplicationContext context = SpringApplication.run(ApiApplicationStart.class, args);

            //服务启动加载
            ServiceStartInit.initSystemConfig(context);
        }catch (Throwable e){
            logger.error("系统启动失败",e);
        }
    }

}