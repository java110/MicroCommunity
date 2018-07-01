package com.java110.store.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class StoreServiceBean {
    @Bean
    public StoreServiceKafka listener() {
        return new StoreServiceKafka();
    }

}
