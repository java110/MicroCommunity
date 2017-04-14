package com.java110.core.event;

/**
 * 商户事件
 * Created by wuxw on 2017/4/14.
 */
public class AppMerchantEvent extends AppEvent {

    private String merchantInfo;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppMerchantEvent(Object source,String merchantInfo) {
        super(source);
        this.merchantInfo = merchantInfo;
    }

    public String getMerchantInfo() {
        return merchantInfo;
    }
}
