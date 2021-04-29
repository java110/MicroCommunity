package com.java110.po.storeOrderCartEvent;

import java.io.Serializable;
import java.util.Date;

public class StoreOrderCartEventPo implements Serializable {

    private String eventId;
private String eventMsg;
private String orderId;
private String cartId;
private String eventObjType;
private String eventObjId;
public String getEventId() {
        return eventId;
    }
public void setEventId(String eventId) {
        this.eventId = eventId;
    }
public String getEventMsg() {
        return eventMsg;
    }
public void setEventMsg(String eventMsg) {
        this.eventMsg = eventMsg;
    }
public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getCartId() {
        return cartId;
    }
public void setCartId(String cartId) {
        this.cartId = cartId;
    }
public String getEventObjType() {
        return eventObjType;
    }
public void setEventObjType(String eventObjType) {
        this.eventObjType = eventObjType;
    }
public String getEventObjId() {
        return eventObjId;
    }
public void setEventObjId(String eventObjId) {
        this.eventObjId = eventObjId;
    }



}
