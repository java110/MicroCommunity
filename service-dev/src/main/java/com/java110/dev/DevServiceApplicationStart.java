package com.java110.dev;

import com.java110.core.annotation.Java110CmdDiscovery;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.cmd.ServiceCmdEventPublishing;
import com.java110.core.log.LoggerFactory;
import com.java110.dev.smo.IDevServiceCacheSMO;
import com.java110.service.init.ServiceStartInit;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
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
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.dev",
        "com.java110.core", "com.java110.config.properties.code", "com.java110.db", "com.java110.utils.factory","com.java110.doc"},
        exclude = {LiquibaseAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@Java110CmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.java110.dev.cmd"})
@EnableFeignClients(basePackages = {"com.java110.intf.user",
        "com.java110.intf.order",
        "com.java110.intf.common",
        "com.java110.intf.community",
        "com.java110.intf.store",
        "com.java110.intf.job"})
public class DevServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(DevServiceApplicationStart.class);

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

    public static void main(String[] args) throws Exception {
        ServiceStartInit.preInitSystemConfig();
        ApplicationContext context = SpringApplication.run(DevServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //刷新缓存
        flushMainCache(args);
//
//        //所有的bean,参考：http://412887952-qq-com.iteye.com/blog/2314051
//        String[] beanNames = context.getBeanDefinitionNames();
//        //String[] beanNames = ctx.getBeanNamesForAnnotation(RestController.class);//所有添加该注解的bean
//        logger.info("bean总数:{}", context.getBeanDefinitionCount());
//        int i = 0;
//        for (String str : beanNames) {
//            logger.info("{},beanName:{}", ++i, str);
//        }

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();

    }

    /**
     * 刷新主要的缓存
     *
     * @param args
     */
    private static void flushMainCache(String[] args) {

        logger.debug("判断是否需要刷新日志，参数 args 为 {}", args);

        //因为好多朋友启动时 不加 参数-Dcache 所以启动时检测 redis 中是否存在 java110_hc_version
        //String mapping = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"java110_hc_version");
        //这里改成强制刷新 因为好多 小伙伴 教不会 如何清理redis 干脆后面就说重启dev 就会刷新缓存 二开的兄弟们根据自己的情况 是否强制
        String mapping = "";
        if (StringUtil.isEmpty(mapping)) {
            IDevServiceCacheSMO devServiceCacheSMOImpl = (IDevServiceCacheSMO) ApplicationContextFactory.getBean("devServiceCacheSMOImpl");
            devServiceCacheSMOImpl.startFlush();
            return;
        }

        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-Dcache")) {
                logger.debug("开始刷新日志，入参为：{}", args[i]);
                IDevServiceCacheSMO devServiceCacheSMOImpl = (IDevServiceCacheSMO) ApplicationContextFactory.getBean("devServiceCacheSMOImpl");
                devServiceCacheSMOImpl.startFlush();
            }
        }
    }
}