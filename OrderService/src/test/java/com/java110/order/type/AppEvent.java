package com.java110.order.type;

import java.util.EventObject;

/**
 * java110 事件
 * Created by wuxw on 2017/4/14.
 */
public class AppEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppEvent(Object source) {
        super(source);
    }
}
