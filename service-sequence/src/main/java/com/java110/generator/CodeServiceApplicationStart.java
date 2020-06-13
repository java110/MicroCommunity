package com.java110.generator;

/**
 * Hello world!
 */

import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.StartException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.config.properties.code.ZookeeperProperties;
import com.java110.service.init.ServiceInfoListener;
import com.java110.service.init.ServiceStartInit;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
@SpringBootApplication(scanBasePackages = {"com.java110.service", "com.java110.code", "com.java110.core",
        "com.java110.config.properties.code", "com.java110.db"})
//@SpringBootApplication(scanBasePackages = {"com.java110.service","com.java110.code","com.java110.config.properties.code"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.java110.core.smo"})
public class CodeServiceApplicationStart {
    protected final static Logger logger = LoggerFactory.getLogger(CodeServiceApplicationStart.class);


    public static void main(String[] args) throws Exception {

        ApplicationContext context = SpringApplication.run(CodeServiceApplicationStart.class, args);

        ServiceStartInit.initSystemConfig(context);

        //加载workId
        loadWorkId();

    }


    /**
     * 加载 workId
     */
    public static void loadWorkId() throws StartException {
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
