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

    private String floorId;
    private String floorNum;
    private String floorArea;
    private String name;
    private String userId;
    private String remark;
    private String communityId;

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
}
