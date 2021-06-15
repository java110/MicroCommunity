package com.java110.core.event.app.pay;

import com.java110.core.context.AppContext;
import com.java110.core.event.app.AppEvent;

/**
 *
 * 支付事件
 * Created by wuxw on 2017/4/14.
 */
public class AppPayEvent extends AppEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppPayEvent(Object source, AppContext context) {
        super(source,context);
    }

}
