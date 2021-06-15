package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;

/**
 * 确认订单调用完成
 * Created by wuxw on 2018/7/2.
 */
public class InvokeConfirmFinishBusinessSystemEvent extends DataFlowEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeConfirmFinishBusinessSystemEvent(Object source, IOrderDataFlowContext dataFlow) {
        super(source, dataFlow);
    }
}
