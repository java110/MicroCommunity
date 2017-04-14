package com.java110.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 事件属性 配置
 * Created by wuxw on 2017/4/14.
 */
@ConfigurationProperties(prefix = "java110.event.properties",locations="classpath:config/event.properties")
public class EventProperties {


    /**
     * 订单调度 侦听
     */
    private String orderDispatchListener;


    public String getOrderDispatchListener() {
        return orderDispatchListener;
    }

    public void setOrderDispatchListener(String orderDispatchListener) {
        this.orderDispatchListener = orderDispatchListener;
    }
}


