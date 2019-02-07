package com.java110.order.kaka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class CenterServiceBean {
    @Bean
    public CenserServiceKafkaListener listener() {
        return new CenserServiceKafkaListener();
    }

}
