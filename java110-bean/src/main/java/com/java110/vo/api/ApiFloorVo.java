package com.java110.vo.api;

import com.java110.vo.Vo;

import java.io.Serializable;

/**
 * API 查询小区楼返回对象
 */
public class ApiFloorVo extends Vo implements Serializable {

    /**
     * floorId
     */
    private String floorId;

    /**
     * 编号
     */
    private String floorNum;

    /**
     * 名称
     */
    private String floorName;

    private String remark;

    private String userName;

    private String createTime;


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

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
