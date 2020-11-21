package com.java110.po.storeOrderCartReturnEvent;

import java.io.Serializable;
import java.util.Date;

public class StoreOrderCartReturnEventPo implements Serializable {

    private String eventId;
private String eventMsg;
private String returnId;
private String eventObjType;
private String eventObjId;
private String storeId;
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
public String getReturnId() {
        return returnId;
    }
public void setReturnId(String returnId) {
        this.returnId = returnId;
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
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }



}
