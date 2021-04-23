package com.java110.config.properties.code;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by wuxw on 2018/6/6.
 */
@Component
@ConfigurationProperties(prefix = "java110.code.zookeeper")
@PropertySource("classpath:config/code/zookeeper.properties")
public class ZookeeperProperties {

    private String url;

    private long port;

    private int timeOut;

    private String workDir;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getZookeeperConnectString(){
        return this.url + ":" +this.port;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }
}
