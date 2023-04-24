package com.java110.dto.storeOrder;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 购物车事件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StoreOrderCartEventDto extends PageDto implements Serializable {

    private String eventId;
    private String eventMsg;
    private String orderId;
    private String cartId;
    private String eventObjType;
    private String eventObjId;


    private Date createTime;

    private String statusCd = "0";


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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
