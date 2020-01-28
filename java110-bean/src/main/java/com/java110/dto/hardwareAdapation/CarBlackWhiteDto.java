package com.java110.dto.hardwareAdapation;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 黑白名单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CarBlackWhiteDto extends PageDto implements Serializable {

    private String blackWhite;
    private String carNum;
    private String startTime;
    private String endTime;
    private String communityId;
    private String bwId;


    private Date createTime;

    private String statusCd = "0";


    public String getBlackWhite() {
        return blackWhite;
    }

    public void setBlackWhite(String blackWhite) {
        this.blackWhite = blackWhite;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getBwId() {
        return bwId;
    }

    public void setBwId(String bwId) {
        this.bwId = bwId;
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
