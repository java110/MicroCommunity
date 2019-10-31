package com.java110.vo.api.complaint;

import java.io.Serializable;
import java.util.Date;

public class ApiComplaintDataVo implements Serializable {

    private String complaintId;
private String storeId;
private String typeCd;
private String roomId;
private String complaintName;
private String tel;
private String state;
private String context;
public String getComplaintId() {
        return complaintId;
    }
public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getTypeCd() {
        return typeCd;
    }
public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }
public String getRoomId() {
        return roomId;
    }
public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
public String getComplaintName() {
        return complaintName;
    }
public void setComplaintName(String complaintName) {
        this.complaintName = complaintName;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }



}
