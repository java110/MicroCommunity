package com.java110.fee.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class FeeServiceBean {
    @Bean
    public FeeServiceKafka listener() {
        return new FeeServiceKafka();
    }

}
