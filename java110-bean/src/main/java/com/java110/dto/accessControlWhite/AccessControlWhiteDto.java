package com.java110.dto.accessControlWhite;

import com.java110.dto.PageDto;
import com.java110.dto.machine.MachineDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 门禁白名单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccessControlWhiteDto extends PageDto implements Serializable {

    public static final String PERSON_TYPE_VISIT = "4004";

    private String thirdId;
    private String idCard;
    private String accessControlKey;
    private String personName;

    private String personNameLike;
    private String machineId;
    private String acwId;
    private String personId;
    private String tel;
    private String startTime;
    private String endTime;
    private String communityId;
    private String personType;
    private String personTypeName;

    private Date createTime;
    private String statusCd = "0";
    private String personFace;

    private String machineCount;


    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAccessControlKey() {
        return accessControlKey;
    }

    public void setAccessControlKey(String accessControlKey) {
        this.accessControlKey = accessControlKey;
    }

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

    public String getAcwId() {
        return acwId;
    }

    public void setAcwId(String acwId) {
        this.acwId = acwId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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


    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getPersonFace() {
        return personFace;
    }

    public void setPersonFace(String personFace) {
        this.personFace = personFace;
    }

    public String getPersonTypeName() {
        return personTypeName;
    }

    public void setPersonTypeName(String personTypeName) {
        this.personTypeName = personTypeName;
    }

    public String getPersonNameLike() {
        return personNameLike;
    }

    public void setPersonNameLike(String personNameLike) {
        this.personNameLike = personNameLike;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(String machineCount) {
        this.machineCount = machineCount;
    }
}
