package com.java110.po.owner;

import java.io.Serializable;

/**
 * @ClassName OwnerRoomRelPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 13:50
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class OwnerRoomRelPo implements Serializable {

    private String relId;
    private String ownerId;
    private String roomId;
    private String state;
    private String userId;
    private String remark;

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
