package com.java110.po.room;

import java.io.Serializable;

/**
 * @ClassName RoomAttrPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 21:06
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class RoomAttrPo implements Serializable {

    private String bId;
    private String attrId;
    private String roomId;
    private String specCd;
    private String value;
    private String statusCd = "0";
    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
