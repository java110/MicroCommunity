package com.java110.po.machineAuth;

import java.io.Serializable;
import java.util.Date;

public class MachineAuthPo implements Serializable {

    private String personName;
private String machineId;
private String personId;
private String startTime;
private String state;
private String endTime;
private String communityId;
private String personType;
private String authId;
public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getPersonId() {
        return personId;
    }
public void setPersonId(String personId) {
        this.personId = personId;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPersonType() {
        return personType;
    }
public void setPersonType(String personType) {
        this.personType = personType;
    }
public String getAuthId() {
        return authId;
    }
public void setAuthId(String authId) {
        this.authId = authId;
    }



}
