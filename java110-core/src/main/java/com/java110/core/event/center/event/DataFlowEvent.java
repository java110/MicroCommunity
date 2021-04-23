package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;

import java.util.EventObject;

/**
 * 数据流事件
 * Created by wuxw on 2018/4/17.
 */
public class DataFlowEvent extends EventObject {


    private IOrderDataFlowContext dataFlow;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DataFlowEvent(Object source, IOrderDataFlowContext dataFlow) {
        super(source);
        this.dataFlow = dataFlow;
    }

    public IOrderDataFlowContext getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(IOrderDataFlowContext dataFlow) {
        this.dataFlow = dataFlow;
    }
}
