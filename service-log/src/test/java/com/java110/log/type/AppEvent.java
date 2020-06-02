package com.java110.log.type;

import com.java110.core.context.AppContext;

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
    public AppEvent(AppContext source) {
        super(source);
    }
    public AppEvent(Object obj,AppContext source) {
        super(source);
    }
}
