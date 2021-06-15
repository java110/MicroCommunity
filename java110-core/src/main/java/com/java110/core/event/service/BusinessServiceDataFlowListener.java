package com.java110.core.event.service;

import com.java110.core.event.app.order.Ordered;

import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface BusinessServiceDataFlowListener extends EventListener,Ordered {

    /**
     * 业务 编码
     * @return
     */
    public String getBusinessTypeCd();

    public void soService(BusinessServiceDataFlowEvent event);
}
