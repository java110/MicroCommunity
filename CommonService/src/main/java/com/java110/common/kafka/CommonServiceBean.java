package com.java110.common.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class CommonServiceBean {
    @Bean
    public CommonServiceKafka listener() {
        return new CommonServiceKafka();
    }

}
