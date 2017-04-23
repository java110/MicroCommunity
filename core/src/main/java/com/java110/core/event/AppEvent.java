package com.java110.core.event;

import com.java110.core.context.AppContext;

import java.util.EventObject;

/**
 * java110 事件
 * Created by wuxw on 2017/4/14.
 */
public class AppEvent extends EventObject {

    private AppContext context;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppEvent(Object source,AppContext context) {
        super(source);
        this.context= context;
    }

    public AppContext getContext() {
        return context;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }
}
