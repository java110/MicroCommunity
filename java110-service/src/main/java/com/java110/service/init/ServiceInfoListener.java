package com.java110.service.init;

import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 系统 启动时读取 服务信息
 * Created by wuxw on 2018/6/6.
 */
@Component
public class ServiceInfoListener implements ApplicationListener<WebServerInitializedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(ServiceInfoListener.class);

    private int serverPort;

    private String serviceHost;

    private long workId;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public String getHostPort(){
        return this.serviceHost + ":" + this.serverPort +"-";
    }
}
