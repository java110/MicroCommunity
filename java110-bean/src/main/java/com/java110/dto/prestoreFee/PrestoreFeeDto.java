package com.java110.dto.prestoreFee;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预存费用数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PrestoreFeeDto extends PageDto implements Serializable {

    private String prestoreFeeAmount;
private String reason;
private String prestoreFeeObjType;
private String prestoreFeeId;
private String remark;
private String state;
private String communityId;
private String prestoreFeeType;
private String roomId;


    private Date createTime;

    private String statusCd = "0";


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
