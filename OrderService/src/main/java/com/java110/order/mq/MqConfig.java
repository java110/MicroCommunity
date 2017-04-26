package com.java110.order.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by wuxw on 2017/4/17.
 */
@ConfigurationProperties(prefix = "mq.queue.name",locations="classpath:mq/mq.properties")
public class MqConfig {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("sample.queue");
    }

    @Bean
    public Topic deleteOrderTopic() {
        return new ActiveMQTopic("sample.topic");
    }
}
