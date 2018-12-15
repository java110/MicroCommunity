package com.java110.agent.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class AgentServiceBean {
    @Bean
    public AgentServiceKafka listener() {
        return new AgentServiceKafka();
    }

}
