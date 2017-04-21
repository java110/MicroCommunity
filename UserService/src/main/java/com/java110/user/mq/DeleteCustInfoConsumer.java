package com.java110.user.mq;

import com.java110.common.log.LoggerEngine;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 作废 客户信息
 * Created by wuxw on 2017/4/17.
 */
@Component
public class DeleteCustInfoConsumer {

    @JmsListener(destination = "sample.queue")
    public void receiveQueue(String text){
        LoggerEngine.debug("消息队列内容："+text);
    }


}
