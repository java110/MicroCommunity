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
package com.java110.order;

import com.java110.config.properties.code.ZookeeperProperties;
import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.trace.Java110RestTemplateInterceptor;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.center.DataFlowEventPublishing;
import com.java110.service.init.ServiceInfoListener;
import com.java110.service.init.ServiceStartInit;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.StartException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
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
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;


/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther com.java110.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.order",
        "com.java110.core", "com.java110.core.event.order", "com.java110.config.properties.code", "com.java110.db"})
@EnableDiscoveryClient
//@EnableConfigurationProperties(EventProperties.class)
@Java110ListenerDiscovery(listenerPublishClass = DataFlowEventPublishing.class,
        basePackages = {"com.java110.order.listener"})
@EnableFeignClients(basePackages = {
        "com.java110.intf.code",
        "com.java110.intf.job",
        "com.java110.intf.user",
        "com.java110.intf.common",
        "com.java110.intf.community",
        "com.java110.intf.fee"
})
@EnableAsync
public class OrderServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceApplicationStart.class);

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
        try {
            ServiceStartInit.preInitSystemConfig();

            ApplicationContext context = SpringApplication.run(OrderServiceApplicationStart.class, args);

            //服务启动加载
            ServiceStartInit.initSystemConfig(context);

            //加载事件数据
            //EventConfigInit.initSystemConfig();


            //加载workId
            loadWorkId();

            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }


    /**
     * 加载 workId
     */
    public static void loadWorkId() throws StartException {

        if (!MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_NEED_INVOKE_GENERATE_ID))) {
            return;
        }
        ZookeeperProperties zookeeperProperties = ApplicationContextFactory.getBean("zookeeperProperties", ZookeeperProperties.class);

        if (zookeeperProperties == null) {
            throw new StartException(ResponseConstant.RESULT_CODE_ERROR, "系统启动失败，未加载zookeeper 配置信息");
        }

        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new StartException(ResponseConstant.RESULT_CODE_ERROR, "系统启动失败，获取host失败" + e);
        }

        ServiceInfoListener serviceInfoListener = ApplicationContextFactory.getBean("serviceInfoListener", ServiceInfoListener.class);

        if (serviceInfoListener == null) {
            throw new StartException(ResponseConstant.RESULT_CODE_ERROR, "系统启动失败，获取服务监听端口失败");
        }

        serviceInfoListener.setServiceHost(host);

        try {
            ZooKeeper zooKeeper = new ZooKeeper(zookeeperProperties.getZookeeperConnectString(), zookeeperProperties.getTimeOut(), new Watcher() {

                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });


            Stat stat = zooKeeper.exists(zookeeperProperties.getWorkDir(), true);

            if (stat == null) {
                zooKeeper.create(zookeeperProperties.getWorkDir(), "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
            String workDir = "";
            List<String> workDirs = zooKeeper.getChildren(zookeeperProperties.getWorkDir(), true);

            if (workDirs != null && workDirs.size() > 0) {
                for (String workDirTemp : workDirs) {
                    if (workDirTemp.startsWith(serviceInfoListener.getHostPort())) {
                        workDir = workDirTemp;
                        break;
                    }
                }
            }
            if (StringUtil.isNullOrNone(workDir)) {
                workDir = zooKeeper.create(zookeeperProperties.getWorkDir() + "/" + serviceInfoListener.getHostPort(), "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT_SEQUENTIAL);
            }

            String[] pathTokens = workDir.split("/");
            if (pathTokens.length > 0
                    && pathTokens[pathTokens.length - 1].contains("-")
                    && pathTokens[pathTokens.length - 1].contains(":")) {
                String workId = pathTokens[pathTokens.length - 1].substring(pathTokens[pathTokens.length - 1].indexOf("-") + 1);
                serviceInfoListener.setWorkId(Long.parseLong(workId));
            }

            Assert.hasLength(serviceInfoListener.getWorkId() + "", "系统中加载workId 失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new StartException(ResponseConstant.RESULT_CODE_ERROR, "系统启动失败，链接zookeeper失败" + zookeeperProperties.getZookeeperConnectString());
        }


    }
}