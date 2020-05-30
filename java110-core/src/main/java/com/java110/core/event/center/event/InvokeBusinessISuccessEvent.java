package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;
import com.java110.entity.order.Business;

/**
 * 调用业务系统事件
 * Created by wuxw on 2018/7/2.
 */
public class InvokeBusinessISuccessEvent extends DataFlowEvent {

    private Business business;

    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeBusinessISuccessEvent(Object source, IOrderDataFlowContext dataFlow, Business business) {
        super(source, dataFlow);
        this.business = business;
    }

    public Business getBusiness() {
        return business;
    }
}
