package com.java110.api.kafka;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.websocket.MessageWebsocket;
import com.java110.api.websocket.ParkingAreaWebsocket;
import com.java110.api.websocket.ParkingBoxWebsocket;
import com.java110.core.base.controller.BaseController;
import com.java110.utils.constant.KafkaConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class FrontServiceKafka extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(FrontServiceKafka.class);


    /**
     * 像前段返回内容
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstant.TOPIC_API_SEND_WEB})
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

    /**
     * 像前段返回内容
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaConstant.TOPIC_API_SEND_PARKING_AREA_WEB})
    public void listenParkingArea(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());

        JSONObject param = null;
        try {
            param = JSONObject.parseObject(record.value().toString());
            ParkingBoxWebsocket.sendInfo(param.toJSONString(), param.getString("extBoxId"));
            ParkingAreaWebsocket.sendInfo(param.toJSONString(), param.getString("extPaId"));
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        } finally {

        }
    }


}
