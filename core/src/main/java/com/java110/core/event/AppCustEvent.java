package com.java110.core.event;

/**
 * 客户事件
 * Created by wuxw on 2017/4/14.
 */
public class AppCustEvent extends AppEvent {

    private String custInfo;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AppCustEvent(Object source,String custInfo) {
        super(source);
        this.custInfo = custInfo;
    }

    public String getCustInfo() {
        return custInfo;
    }
}
