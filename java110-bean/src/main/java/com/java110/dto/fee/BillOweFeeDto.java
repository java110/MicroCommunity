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
    public static final String STATE_WILL_FEE = "1000";//未缴费
    public static final String STATE_SEND_OWNER = "3000";//已催缴

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
    private String stateName;
    private String createTime;
    private String[] ownerIds;
    private String deadlineTime;
    private String payObjId;
    private String curBill;
    private String feeTypeCd;
    private String feeTypeName;

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

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

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getPayObjId() {
        return payObjId;
    }

    public void setPayObjId(String payObjId) {
        this.payObjId = payObjId;
    }

    public String getCurBill() {
        return curBill;
    }

    public void setCurBill(String curBill) {
        this.curBill = curBill;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
