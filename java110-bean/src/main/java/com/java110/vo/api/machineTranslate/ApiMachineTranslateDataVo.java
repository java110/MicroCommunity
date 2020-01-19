package com.java110.vo.api.machineTranslate;

import java.io.Serializable;
import java.util.Date;

public class ApiMachineTranslateDataVo implements Serializable {

    private String machineTranslateId;
    private String machineCode;
    private String machineId;
    private String typeCd;
    private String typeCdName;
    private String objName;
    private String objId;
    private String state;
    private String stateName;
    private String createTime;
    private String updateTime;

    public String getMachineTranslateId() {
        return machineTranslateId;
    }

    public void setMachineTranslateId(String machineTranslateId) {
        this.machineTranslateId = machineTranslateId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTypeCdName() {
        return typeCdName;
    }

    public void setTypeCdName(String typeCdName) {
        this.typeCdName = typeCdName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
