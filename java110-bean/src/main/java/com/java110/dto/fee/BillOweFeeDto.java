package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @ClassName BillOweFeeDto
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 17:42
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class BillOweFeeDto extends PageDto implements Serializable {

    public static final String STATE_FINISH_FEE = "2000";//已缴费

    private String oweId;
    private String billId;
    private String feeId;
    private String billAmountOwed;
    private String amountOwed;
    private String feeEndTime;
    private String ownerId;
    private String ownerName;
    private String ownerTel;
    private String payerObjName;
    private String payerObjType;
    private String communityId;
    private String state;
    private String createTime;
    private String[] ownerIds;


    public String getOweId() {
        return oweId;
    }

    public void setOweId(String oweId) {
        this.oweId = oweId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getBillAmountOwed() {
        return billAmountOwed;
    }

    public void setBillAmountOwed(String billAmountOwed) {
        this.billAmountOwed = billAmountOwed;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getFeeEndTime() {
        return feeEndTime;
    }

    public void setFeeEndTime(String feeEndTime) {
        this.feeEndTime = feeEndTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String[] getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(String[] ownerIds) {
        this.ownerIds = ownerIds;
    }
}
