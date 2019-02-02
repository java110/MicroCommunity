package com.java110.center.kaka;

import com.java110.center.smo.ICenterServiceSMO;
import com.java110.core.base.AppBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class CenserServiceKafkaListener extends AppBase {

    protected final static Logger logger = LoggerFactory.getLogger(CenserServiceKafkaListener.class);


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
