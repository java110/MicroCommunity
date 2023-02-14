package com.java110.po.car;

import java.io.Serializable;

/**
 * @ClassName CarBlackWhitePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 14:18
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class CarBlackWhitePo implements Serializable {

    private String bwId;
    private String communityId;
    private String blackWhite;
    private String carNum;
    private String startTime;
    private String endTime;
    private String paId;
    private String statusCd="0";
    private String bId;
    private String createTime;

    public String getBwId() {
        return bwId;
    }

    public void setBwId(String bwId) {
        this.bwId = bwId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

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

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
