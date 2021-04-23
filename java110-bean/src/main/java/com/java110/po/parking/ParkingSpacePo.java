package com.java110.po.parking;

import java.io.Serializable;

/**
 * @ClassName ParkingSpacePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:49
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class ParkingSpacePo implements Serializable {
    private String psId;
    private String communityId;
    private String num;
    private String paId;
    private String state;
    private String area;
    private String remark;
    private String parkingType;
    private String parkingTypeName;
    private String createTime;
    private String statusCd = "0";
    private String bId;

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getParkingTypeName() {
        return parkingTypeName;
    }

    public void setParkingTypeName(String parkingTypeName) {
        this.parkingTypeName = parkingTypeName;
    }
}
