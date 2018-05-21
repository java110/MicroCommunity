package com.java110.center.kaka;

import com.java110.center.smo.ICenterServiceSMO;
import com.java110.core.base.AppBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class CenserServiceKafkaListener extends AppBase {

    @Autowired
    private ICenterServiceSMO centerServiceSMOImpl;

    @KafkaListener(topics = {"NOTIFY_CENTER_SERVICE"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());
        centerServiceSMOImpl.receiveBusinessSystemNotifyMessage(record.value().toString());
    }


    public ICenterServiceSMO getCenterServiceSMOImpl() {
        return centerServiceSMOImpl;
    }

    public void setCenterServiceSMOImpl(ICenterServiceSMO centerServiceSMOImpl) {
        this.centerServiceSMOImpl = centerServiceSMOImpl;
    }
}
