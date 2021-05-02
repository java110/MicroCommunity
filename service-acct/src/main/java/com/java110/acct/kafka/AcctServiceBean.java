package com.java110.acct.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class AcctServiceBean {
    @Bean
    public AcctServiceKafka listener() {
        return new AcctServiceKafka();
    }

}
