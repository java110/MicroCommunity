package com.java110.comment.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wuxw on 2018/4/15.
 */
@Configuration
public class CommentServiceBean {
    @Bean
    public CommentServiceKafka listener() {
        return new CommentServiceKafka();
    }

}
