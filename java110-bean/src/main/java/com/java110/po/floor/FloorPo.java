package com.java110.po.floor;

import java.io.Serializable;

/**
 * @ClassName FloorPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 13:09
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class FloorPo implements Serializable {

    public static final String FLOOR_ATTR_LARGE = "100201912001";//大厦楼栋 属性
    public static final String FLOOR_ATTR_VALUE = "8008";//大厦楼栋 属性值

    private String floorId;
    private String floorNum;
    private String floorArea;
    private String name;
    private String userId;
    private String bId;
    private String remark;
    private String communityId;
    private String statusCd = "0";
    private int seq;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
