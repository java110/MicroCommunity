package com.java110.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 订单相关配置信息
 * Created by wuxw on 2017/4/25.
 */

@ConfigurationProperties(prefix = "java110.order",locations="classpath:config/order.properties")
@Component("orderProperties")
public class OrderProperties {

    private String deleteOrderAsyn;



    public String getDeleteOrderAsyn() {
        return deleteOrderAsyn;
    }

    public void setDeleteOrderAsyn(String deleteOrderAsyn) {
        this.deleteOrderAsyn = deleteOrderAsyn;
    }
}
