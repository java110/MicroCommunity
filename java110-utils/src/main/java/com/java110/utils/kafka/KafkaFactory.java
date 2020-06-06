package com.java110.utils.kafka;

import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka 工厂类
 * Created by wuxw on 2018/4/15.
 */
public class KafkaFactory {

    /**
     * 获取kafka template
     * @return
     */
    private static KafkaTemplate getKafkaTemplate(){
        return (KafkaTemplate) ApplicationContextFactory.getBean("kafkaTemplate");
    }

    /**
     * 发送kafka消息
     * @param topic
     * @param key
     * @param message
     * @throws Exception
     */
    public static void sendKafkaMessage(String topic,String key,Object message) throws Exception{
        getKafkaTemplate().send(topic,key,message);
    }

    /**
     * 发送kafka消息
     * @param topic
     * @param message
     * @throws Exception
     */
    public static void sendKafkaMessage(String topic,Object message) throws Exception{
        getKafkaTemplate().send(topic,"",message);
    }
}
