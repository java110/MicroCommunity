package com.java110.vo.api.carBlackWhite;

import java.io.Serializable;
import java.util.Date;

public class ApiCarBlackWhiteDataVo implements Serializable {

    private String bwId;
    private String blackWhite;
    private String blackWhiteName;
    private String carNum;
    private String startTime;
    private String endTime;
    private String paId;
    private String paNum;

    public String getBwId() {
        return bwId;
    }

    public void setBwId(String bwId) {
        this.bwId = bwId;
    }

    public String getBlackWhite() {
        return blackWhite;
    }

    public void setBlackWhite(String blackWhite) {
        this.blackWhite = blackWhite;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBlackWhiteName() {
        return blackWhiteName;
    }

    public void setBlackWhiteName(String blackWhiteName) {
        this.blackWhiteName = blackWhiteName;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }
}
