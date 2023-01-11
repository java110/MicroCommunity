package com.java110.dto.itemRelease;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 放行管理数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ItemReleaseDto extends PageDto implements Serializable {

    private String amount;
private String applyCompany;
private String idCard;
private String passTime;
private String carNum;
private String remark;
private String applyPerson;
private String irId;
private String applyTel;
private String typeId;
private String state;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }
public void setAmount(String amount) {
        this.amount = amount;
    }
public String getApplyCompany() {
        return applyCompany;
    }
public void setApplyCompany(String applyCompany) {
        this.applyCompany = applyCompany;
    }
public String getIdCard() {
        return idCard;
    }
public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
public String getPassTime() {
        return passTime;
    }
public void setPassTime(String passTime) {
        this.passTime = passTime;
    }
public String getCarNum() {
        return carNum;
    }
public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getApplyPerson() {
        return applyPerson;
    }
public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }
public String getIrId() {
        return irId;
    }
public void setIrId(String irId) {
        this.irId = irId;
    }
public String getApplyTel() {
        return applyTel;
    }
public void setApplyTel(String applyTel) {
        this.applyTel = applyTel;
    }
public String getTypeId() {
        return typeId;
    }
public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
