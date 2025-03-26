package com.java110.dto.floorShareFee;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 公摊费用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FloorShareFeeDto extends PageDto implements Serializable {

    private String amount;
private String ownerName;
private String fsfId;
private String fsmId;
private String feeName;
private String remark;
private String readingId;
private String feeId;
private String roomName;
private String degrees;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getOwnerName() {
        return ownerName;
    }
public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
public String getFsfId() {
        return fsfId;
    }
public void setFsfId(String fsfId) {
        this.fsfId = fsfId;
    }
public String getFsmId() {
        return fsmId;
    }
public void setFsmId(String fsmId) {
        this.fsmId = fsmId;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getReadingId() {
        return readingId;
    }
public void setReadingId(String readingId) {
        this.readingId = readingId;
    }
public String getFeeId() {
        return feeId;
    }
public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
public String getRoomName() {
        return roomName;
    }
public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
public String getDegrees() {
        return degrees;
    }
public void setDegrees(String degrees) {
        this.degrees = degrees;
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
