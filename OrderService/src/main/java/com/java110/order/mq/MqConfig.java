package com.java110.order.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * Created by wuxw on 2017/4/17.
 */
@ConfigurationProperties(prefix = "mq.queue.name",locations="classpath:mq/mq.properties")
public class MqConfig {

    @Autowired
    ConnectionFactory mqConnectionFactory;

    @Autowired
    PooledConnectionFactory pooledConnectionFactory;

    @Value("user")
    private String userQueueName;

    @Value("deleteOrderQueue")
    private String deleteOrderQueueName;
    @Value("deleteOrderTopic")
    private String deleteOrderTopicName;

    private String brokerUrl;

    private String username;

    private String password;

    private String maxConnection;

    /**
     * 默认最大链接数
     */
    private final static int DEFAULT_MAX_CONNECTION = 50;


    /**
     * activemq 链接工厂
     * 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供
     * @return
     */
    @Bean(name="mqConnectionFactory")
    public ConnectionFactory mqConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * 池链接
     * ActiveMQ为我们提供了一个PooledConnectionFactory，通过往里面注入一个ActiveMQConnectionFactory
     可以用来将Connection、Session和MessageProducer池化，这样可以大大的减少我们的资源消耗。 要依赖于 activemq-pool包
     * @return
     */
    @Bean(name="pooledConnectionFactory")
    public PooledConnectionFactory pooledConnectionFactory(){

        PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
        connectionFactory.setConnectionFactory(mqConnectionFactory);
        connectionFactory.setMaxConnections(NumberUtils.isNumber(maxConnection)?NumberUtils.toInt(maxConnection):DEFAULT_MAX_CONNECTION);
        return connectionFactory;
    }

    @Bean(name="deleteOrderQueueJmsTemplate")
    public JmsTemplate deleteOrderQueueJmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(pooledConnectionFactory);
        jmsTemplate.setDefaultDestinationName(deleteOrderQueueName);
        return jmsTemplate;
    }
    public String getUserQueueName() {
        return userQueueName;
    }

    public void setUserQueueName(String userQueueName) {
        this.userQueueName = userQueueName;
    }

    public String getDeleteOrderQueueName() {
        return deleteOrderQueueName;
    }

    public void setDeleteOrderQueueName(String deleteOrderQueueName) {
        this.deleteOrderQueueName = deleteOrderQueueName;
    }

    public String getDeleteOrderTopicName() {
        return deleteOrderTopicName;
    }

    public void setDeleteOrderTopicName(String deleteOrderTopicName) {
        this.deleteOrderTopicName = deleteOrderTopicName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public ConnectionFactory getMqConnectionFactory() {
        return mqConnectionFactory;
    }

    public void setMqConnectionFactory(ConnectionFactory mqConnectionFactory) {
        this.mqConnectionFactory = mqConnectionFactory;
    }

    public String getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(String maxConnection) {
        this.maxConnection = maxConnection;
    }

    public PooledConnectionFactory getPooledConnectionFactory() {
        return pooledConnectionFactory;
    }

    public void setPooledConnectionFactory(PooledConnectionFactory pooledConnectionFactory) {
        this.pooledConnectionFactory = pooledConnectionFactory;
    }
}
