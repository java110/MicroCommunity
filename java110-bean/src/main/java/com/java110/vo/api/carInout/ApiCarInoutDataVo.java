package com.java110.vo.api.carInout;

import java.io.Serializable;
import java.util.Date;

public class ApiCarInoutDataVo implements Serializable {

    private String inoutId;
    private String state;
    private String stateName;
    private String carNum;
    private String inTime;
    private String outTime;
    private int inHours;
    private int inMin;
    private double money;

    public String getInoutId() {
        return inoutId;
    }

    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
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

    public int getInHours() {
        return inHours;
    }

    public void setInHours(int inHours) {
        this.inHours = inHours;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getInMin() {
        return inMin;
    }

    public void setInMin(int inMin) {
        this.inMin = inMin;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
