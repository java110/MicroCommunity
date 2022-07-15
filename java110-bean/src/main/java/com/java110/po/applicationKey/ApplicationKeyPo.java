package com.java110.po.applicationKey;

import java.io.Serializable;

/**
 * @ClassName ApplicationKeyPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/26 22:20
 * @Version 1.0
 * add by wuxw 2020/5/26
 **/
public class ApplicationKeyPo implements Serializable {

    private String applicationKeyId;
    private String machineId;
    private String typeCd;
    private String name;
    private String tel;
    private String sex;
    private String age;
    private String idCard;
    private String communityId;
    private String state;
    private String startTime;
    private String endTime;
    private String typeFlag;
    private String pwd;
    private String statusCd;



    public String getApplicationKeyId() {
        return applicationKeyId;
    }

    public void setApplicationKeyId(String applicationKeyId) {
        this.applicationKeyId = applicationKeyId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
