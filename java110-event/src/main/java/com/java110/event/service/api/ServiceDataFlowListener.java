package com.java110.event.service.api;

import com.java110.event.app.order.Ordered;
import com.java110.event.service.BusinessServiceDataFlowEvent;

import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface ServiceDataFlowListener extends EventListener,Ordered {

    /**
     * 业务 编码
     * @return
     */
    public String getServiceCode();

    public void soService(ServiceDataFlowEvent event);
}
