package com.java110.event.center.event;

import com.java110.core.context.DataFlow;

/**
 * 加载配置文件完成
 * Created by wuxw on 2018/7/2.
 */
public class LoadConfigDataCompleteEvent extends DataFlowEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public LoadConfigDataCompleteEvent(Object source, DataFlow dataFlow) {
        super(source, dataFlow);
    }
}
