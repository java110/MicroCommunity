package com.java110.event.center.merchant.listener;

import com.java110.common.log.LoggerEngine;
import com.java110.core.context.DataFlow;
import com.java110.event.app.order.Ordered;
import com.java110.event.center.DataFlowListener;
import com.java110.event.center.merchant.event.DataFlowMerchantEvent;
import org.springframework.stereotype.Component;

/**
 * Created by wuxw on 2018/4/17.
 */
@Component
public class DataFlowMerchantListener extends LoggerEngine implements DataFlowListener<DataFlowMerchantEvent>,Ordered {
    @Override
    public void soService(DataFlowMerchantEvent event) {
        DataFlow dataFlow = event.getDataFlow();
        //这里处理业务逻辑
        logger.info("DataFlowCustListener 侦听执行");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
