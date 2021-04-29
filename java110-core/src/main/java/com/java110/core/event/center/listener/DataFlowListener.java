package com.java110.core.event.center.listener;



import com.java110.core.event.center.event.DataFlowEvent;

import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface DataFlowListener <E extends DataFlowEvent> extends EventListener {

    void soService(E event);
}
