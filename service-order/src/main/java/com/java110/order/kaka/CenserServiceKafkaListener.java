package com.java110.order.kaka;

import com.java110.core.base.AppBase;
import com.java110.order.smo.ICenterServiceSMO;
import com.java110.order.smo.IOrderServiceSMO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class CenserServiceKafkaListener extends AppBase {

    protected final static Logger logger = LoggerFactory.getLogger(CenserServiceKafkaListener.class);


    @Autowired
    private IOrderServiceSMO orderServiceSMOImpl;

    @KafkaListener(topics = {"NOTIFY_CENTER_SERVICE"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());
        orderServiceSMOImpl.receiveBusinessSystemNotifyMessage(record.value().toString());
    }


    public IOrderServiceSMO getOrderServiceSMOImpl() {
        return orderServiceSMOImpl;
    }

    public void setOrderServiceSMOImpl(IOrderServiceSMO orderServiceSMOImpl) {
        this.orderServiceSMOImpl = orderServiceSMOImpl;
    }
}
