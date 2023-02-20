package com.java110.vo.api;

import java.io.Serializable;

/**
 * @ClassName ApiUnitVo
 * @Description TODO 小区单元数据分装返回
 * @Author wuxw
 * @Date 2019/5/2 20:35
 * @Version 1.0
 * add by wuxw 2019/5/2
 **/
public class ApiUnitVo implements Serializable {

    //小区单元ID
    private String unitId;

    //小区楼ID
    private String floorId;


    private String floorNum;
    private String floorName;
    //单元编号
    private String unitNum;

    //小区总层数
    private String layerCount;

    //是否有电梯
    private String lift;

    //创建人名称
    private String userName;

    //备注
    private String remark;

    private String unitArea;

    //创建时间
    private String createTime;

    private int seq;


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUnitArea() {
        return unitArea;
    }

    public void setUnitArea(String unitArea) {
        this.unitArea = unitArea;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
