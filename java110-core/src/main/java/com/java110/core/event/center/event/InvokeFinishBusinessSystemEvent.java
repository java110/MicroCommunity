package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;

/**
 * 调用业务系统事件
 * Created by wuxw on 2018/7/2.
 */
public class InvokeFinishBusinessSystemEvent extends DataFlowEvent {

    /**
     * Constructs a prototypical Event.
     *Confirm
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeFinishBusinessSystemEvent(Object source, IOrderDataFlowContext dataFlow) {
        super(source, dataFlow);
    }
}
