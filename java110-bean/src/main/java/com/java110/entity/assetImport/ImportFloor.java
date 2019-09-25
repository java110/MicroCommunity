package com.java110.entity.assetImport;

/**
 * 小区楼对象
 */
public class ImportFloor {

    private String floorId;

    private String floorNum;

    private String unitId;

    private String unitNum;

    private String layerCount;

    private String lift;


    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
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

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
