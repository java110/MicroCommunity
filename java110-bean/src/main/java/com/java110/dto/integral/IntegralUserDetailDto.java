package com.java110.dto.integral;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分核销数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralUserDetailDto extends PageDto implements Serializable {

    private String createUserId;
private String uoId;
private String acctId;
private String remark;
private String acctName;
private String userName;
private String detailType;
private String useQuantity;
private String money;
private String acctDetailId;
private String businessKey;
private String tel;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getUoId() {
        return uoId;
    }
public void setUoId(String uoId) {
        this.uoId = uoId;
    }
public String getAcctId() {
        return acctId;
    }
public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getAcctName() {
        return acctName;
    }
public void setAcctName(String acctName) {
        this.acctName = acctName;
    }
public String getUserName() {
        return userName;
    }
public void setUserName(String userName) {
        this.userName = userName;
    }
public String getDetailType() {
        return detailType;
    }
public void setDetailType(String detailType) {
        this.detailType = detailType;
    }
public String getUseQuantity() {
        return useQuantity;
    }
public void setUseQuantity(String useQuantity) {
        this.useQuantity = useQuantity;
    }
public String getMoney() {
        return money;
    }
public void setMoney(String money) {
        this.money = money;
    }
public String getAcctDetailId() {
        return acctDetailId;
    }
public void setAcctDetailId(String acctDetailId) {
        this.acctDetailId = acctDetailId;
    }
public String getBusinessKey() {
        return businessKey;
    }
public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
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
