package com.java110.event.center;

import com.java110.core.context.DataFlow;

import java.util.EventObject;

/**
 * 数据流事件
 * Created by wuxw on 2018/4/17.
 */
public class DataFlowEvent extends EventObject {


    private DataFlow dataFlow;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DataFlowEvent(Object source, DataFlow dataFlow) {
        super(source);
        this.dataFlow = dataFlow;
    }

    public DataFlow getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(DataFlow dataFlow) {
        this.dataFlow = dataFlow;
    }
}
