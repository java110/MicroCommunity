package com.java110.core.event.service;

import com.java110.core.context.DataFlowContext;

import java.util.EventObject;

/**
 * Created by wuxw on 2018/5/18.
 */
public class BusinessServiceDataFlowEvent extends EventObject {

    private DataFlowContext dataFlowContext;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BusinessServiceDataFlowEvent(Object source,DataFlowContext dataFlowContext) {
        super(source);
        this.dataFlowContext = dataFlowContext;
    }


    public DataFlowContext getDataFlowContext() {
        return dataFlowContext;
    }

    /*public void setDataFlowContext(DataFlowContext dataFlowContext) {
        this.dataFlowContext = dataFlowContext;
    }*/
}
