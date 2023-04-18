package com.java110.dto.owner;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主入驻房屋数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerSettledRoomsDto extends PageDto implements Serializable {

    private String applyId;
private String startTime;
private String osrId;
private String endTime;
private String communityId;
private String roomId;
private String roomName;


    private Date createTime;

    private String statusCd = "0";


    public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getOsrId() {
        return osrId;
    }
public void setOsrId(String osrId) {
        this.osrId = osrId;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getRoomId() {
        return roomId;
    }
public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
public String getRoomName() {
        return roomName;
    }
public void setRoomName(String roomName) {
        this.roomName = roomName;
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
