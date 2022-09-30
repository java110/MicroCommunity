package com.java110.dto.communitySpacePerson;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 小区场地预约数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommunitySpacePersonDto extends PageDto implements Serializable {

    private String receivableAmount;
private String payWay;
private String remark;
private String receivedAmount;
private String personName;
private String spaceId;
private String cspId;
private String appointmentTime;
private String personId;
private String state;
private String personTel;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getReceivableAmount() {
        return receivableAmount;
    }
public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
public String getPayWay() {
        return payWay;
    }
public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getReceivedAmount() {
        return receivedAmount;
    }
public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }
public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getSpaceId() {
        return spaceId;
    }
public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
public String getCspId() {
        return cspId;
    }
public void setCspId(String cspId) {
        this.cspId = cspId;
    }
public String getAppointmentTime() {
        return appointmentTime;
    }
public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
public String getPersonId() {
        return personId;
    }
public void setPersonId(String personId) {
        this.personId = personId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getPersonTel() {
        return personTel;
    }
public void setPersonTel(String personTel) {
        this.personTel = personTel;
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
