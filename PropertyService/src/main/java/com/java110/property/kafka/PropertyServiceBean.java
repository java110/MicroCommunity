package com.java110.property.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class PropertyServiceBean {
    @Bean
    public PropertyServiceKafka listener() {
        return new PropertyServiceKafka();
    }

}
