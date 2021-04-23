package com.java110.front.kafka;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.controller.BaseController;
import com.java110.front.websocket.MessageWebsocket;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class FrontServiceKafka extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(FrontServiceKafka.class);


    @KafkaListener(topics = {"webSentMessageTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());

        JSONObject param = null;
        try {
            param = JSONObject.parseObject(record.value().toString());
            MessageWebsocket.sendInfo(param.toJSONString(), param.getString("userId"));
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        } finally {

        }
    }


}
