package com.java110.vo.api.machineTranslate;

import java.io.Serializable;
import java.util.Date;

public class ApiMachineTranslateDataVo implements Serializable {

    private String machineTranslateId;
private String machineCode;
private String machineId;
private String typeCd;
private String objName;
private String objId;
private String state;
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



}
