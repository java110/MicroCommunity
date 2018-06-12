package com.java110.log.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class LogServiceBean {
    @Bean
    public LogServiceKafka listener() {
        return new LogServiceKafka();
    }

}
