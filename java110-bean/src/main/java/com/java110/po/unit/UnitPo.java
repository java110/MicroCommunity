package com.java110.po.unit;

import java.io.Serializable;

/**
 * @ClassName UnitPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 22:09
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class UnitPo implements Serializable {

    private String unitId;
    private String unitNum;
    private String floorId;
    private String layerCount;
    private String lift;
    private String userId;
    private String remark;
    private String unitArea;
    private String statusCd = "0";

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(String layerCount) {
        this.layerCount = layerCount;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
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

    public String getUnitArea() {
        return unitArea;
    }

    public void setUnitArea(String unitArea) {
        this.unitArea = unitArea;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
