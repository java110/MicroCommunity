package com.java110.shop.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class ShopServiceBean {
    @Bean
    public ShopServiceKafka listener() {
        return new ShopServiceKafka();
    }

}
