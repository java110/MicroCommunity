package com.java110.community.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class CommunityServiceBean {
    @Bean
    public CommunityServiceKafka listener() {
        return new CommunityServiceKafka();
    }

}
