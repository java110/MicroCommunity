package com.java110.dto.account;

import com.java110.dto.PageDto;
import com.java110.dto.account.AccountDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预存单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AccountReceiptDto extends AccountDto implements Serializable {

    private String arId;
    private String[] arIds;
    private String ownerName;
    private String primeRate;
    private String link;
    private String acctId;
    private String receivableAmount;
    private String receivedAmount;
    private String communityId;
    private String ownerId;

    private String remark;


    private Date createTime;

    private String statusCd = "0";

    private String primeRateName;

    private String acctTypeName;


    public String getArId() {
        return arId;
    }

    public void setArId(String arId) {
        this.arId = arId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrimeRateName() {
        return primeRateName;
    }

    public void setPrimeRateName(String primeRateName) {
        this.primeRateName = primeRateName;
    }

    @Override
    public String getAcctTypeName() {
        return acctTypeName;
    }

    @Override
    public void setAcctTypeName(String acctTypeName) {
        this.acctTypeName = acctTypeName;
    }

    public String[] getArIds() {
        return arIds;
    }

    public void setArIds(String[] arIds) {
        this.arIds = arIds;
    }
}
