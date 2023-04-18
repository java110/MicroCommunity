package com.java110.dto.community;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区场地数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommunitySpaceDto extends PageDto implements Serializable {

    private String adminName;
    private String spaceId;
    private String name;
    private String startTime;
    private String tel;
    private String endTime;
    private String state;
    private String communityId;
    private String feeMoney;

    private String venueId;

    private Date createTime;

    private String statusCd = "0";

    private List<CommunitySpaceOpenTimeDto> openTimes;


    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(String feeMoney) {
        this.feeMoney = feeMoney;
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

    public List<CommunitySpaceOpenTimeDto> getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(List<CommunitySpaceOpenTimeDto> openTimes) {
        this.openTimes = openTimes;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
}
