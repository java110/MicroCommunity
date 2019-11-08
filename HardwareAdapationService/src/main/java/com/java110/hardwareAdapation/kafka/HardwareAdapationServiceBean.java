package com.java110.hardwareAdapation.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class HardwareAdapationServiceBean {
    @Bean
    public HardwareAdapationServiceKafka listener() {
        return new HardwareAdapationServiceKafka();
    }

}
