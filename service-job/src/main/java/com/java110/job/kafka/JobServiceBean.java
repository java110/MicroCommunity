package com.java110.job.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class JobServiceBean {
    @Bean
    public JobServiceKafka listener() {
        return new JobServiceKafka();
    }


}
