package com.java110.event.center.merchant.event;

import com.java110.entity.center.DataFlow;
import com.java110.event.center.DataFlowEvent;

/**
 * 客户事件处理
 * Created by wuxw on 2018/4/17.
 */
public class DataFlowMerchantEvent extends DataFlowEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public DataFlowMerchantEvent(Object source, DataFlow dataFlow) {
        super(source, dataFlow);
    }
}
