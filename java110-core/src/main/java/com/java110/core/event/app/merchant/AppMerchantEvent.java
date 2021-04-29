package com.java110.core.event.app.merchant;

import com.java110.core.context.AppContext;
import com.java110.core.event.app.AppEvent;

/**
 * 商户事件
 * Created by wuxw on 2017/4/14.
 */
public class AppMerchantEvent extends AppEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppMerchantEvent(Object source, AppContext context) {
        super(source,context);
    }
}
