package com.java110.po.car;

import java.io.Serializable;

/**
 * @ClassName CarInout
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/27 14:31
 * @Version 1.0
 * add by wuxw 2020/5/27
 **/
public class CarInoutPo implements Serializable {

    private String inoutId;
    private String communityId;
    private String carNum;
    private String state;
    private String inTime;
    private String outTime;


    public String getInoutId() {
        return inoutId;
    }

    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
