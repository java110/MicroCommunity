package com.java110.order.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;


/**
 * 作废订单信息
 *
 * Created by wuxw on 2017/4/17.
 */
@Component
public class DeleteOrderInfoProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Topic deleteOrderTopic;


    /**
     * 发布消息
     * @param msg
     */
    public void send(String msg) {
        this.jmsMessagingTemplate.convertAndSend(deleteOrderTopic, msg);
    }
}
