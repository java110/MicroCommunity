package com.java110.core.event;

/**
 *
 * 支付事件
 * Created by wuxw on 2017/4/14.
 */
public class AppPayEvent extends AppEvent {

    private String payInfo;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppPayEvent(Object source,String payInfo) {
        super(source);
        this.payInfo= payInfo;
    }

    public String getPayInfo() {
        return payInfo;
    }
}
