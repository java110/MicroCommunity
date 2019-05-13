package com.java110.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerDto extends PageDto implements Serializable {


    private String communityId;

    private String roomId;
    private String sex;
private String name;
private String link;
private String remark;
private String ownerId;
private String userId;
private String age;
private String memberId;


    private Date createTime;

    private String statusCd = "0";


    public String getSex() {
        return sex;
    }
public void setSex(String sex) {
        this.sex = sex;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getLink() {
        return link;
    }
public void setLink(String link) {
        this.link = link;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getOwnerId() {
        return ownerId;
    }
public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getAge() {
        return age;
    }
public void setAge(String age) {
        this.age = age;
    }
public String getMemberId() {
        return memberId;
    }
public void setMemberId(String memberId) {
        this.memberId = memberId;
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
}
