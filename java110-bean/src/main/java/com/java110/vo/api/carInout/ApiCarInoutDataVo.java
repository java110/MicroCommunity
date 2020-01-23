package com.java110.vo.api.carInout;

import java.io.Serializable;
import java.util.Date;

public class ApiCarInoutDataVo implements Serializable {

    private String inoutId;
private String state;
private String carNum;
private String inTime;
private String outTime;
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



}
