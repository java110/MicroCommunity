package com.java110.order.listener;

import com.java110.core.annotation.Java110Listener;
import com.java110.core.event.app.order.Ordered;
import com.java110.core.event.center.event.ReceiveRequestEvent;
import com.java110.core.event.center.listener.DataFlowListener;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.Map;

/**
 * Created by wuxw on 2018/7/2.
 */
@Java110Listener(name = "receiveRequestListener")
public class ReceiveRequestListener implements DataFlowListener<ReceiveRequestEvent>,Ordered {

    private final static Logger logger = LoggerFactory.getLogger(ReceiveRequestListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void soService(ReceiveRequestEvent event) {

        Map<String,String> headers = event.getHeaders();
        logger.debug("请求头信息为：{}",headers);
        String requestData = event.getRequestData();
        logger.debug("请求报文为：{}",requestData);
    }
}
