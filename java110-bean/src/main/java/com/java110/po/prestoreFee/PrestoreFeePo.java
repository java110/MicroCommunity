package com.java110.po.prestoreFee;

import java.io.Serializable;
import java.util.Date;

public class PrestoreFeePo implements Serializable {

    private String prestoreFeeAmount;
private String reason;
private String prestoreFeeObjType;
private String prestoreFeeId;
private String remark;
private String statusCd = "0";
private String state;
private String communityId;
private String prestoreFeeType;
private String roomId;
public String getPrestoreFeeAmount() {
        return prestoreFeeAmount;
    }
public void setPrestoreFeeAmount(String prestoreFeeAmount) {
        this.prestoreFeeAmount = prestoreFeeAmount;
    }
public String getReason() {
        return reason;
    }
public void setReason(String reason) {
        this.reason = reason;
    }
public String getPrestoreFeeObjType() {
        return prestoreFeeObjType;
    }
public void setPrestoreFeeObjType(String prestoreFeeObjType) {
        this.prestoreFeeObjType = prestoreFeeObjType;
    }
public String getPrestoreFeeId() {
        return prestoreFeeId;
    }
public void setPrestoreFeeId(String prestoreFeeId) {
        this.prestoreFeeId = prestoreFeeId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPrestoreFeeType() {
        return prestoreFeeType;
    }
public void setPrestoreFeeType(String prestoreFeeType) {
        this.prestoreFeeType = prestoreFeeType;
    }
public String getRoomId() {
        return roomId;
    }
public void setRoomId(String roomId) {
        this.roomId = roomId;
    }



}
