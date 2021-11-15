package com.java110.dto.reportOweFee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 欠费统计数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportOweFeeDto extends PageDto implements Serializable {

    private String configName;
    private String deadlineTime;
    private String ownerTel;
    private String updateTime;
    private String oweId;
    private String ownerId;
    private String feeId;
    private String amountOwed;
    private String payerObjName;
    private String ownerName;
    private String configId;
    private String[] configIds;
    private String feeName;
    private String endTime;
    private String communityId;
    private String payerObjType;
    private String payerObjId;
    private String[] payerObjIds;
    private String hasOweFee;
    private String floorId;
    private String unitId;
    private String roomSubType;
    private String roomNum;
    private double totalOweAmount;
    List<ReportOweFeeItemDto> items;
    List<ReportOweFeeItemDto> itemTotalOweAmounts;


    private Date createTime;

    private String statusCd = "0";


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOweId() {
        return oweId;
    }

    public void setOweId(String oweId) {
        this.oweId = oweId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
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

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
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

    public String[] getPayerObjIds() {
        return payerObjIds;
    }

    public void setPayerObjIds(String[] payerObjIds) {
        this.payerObjIds = payerObjIds;
    }

    public List<ReportOweFeeItemDto> getItems() {
        return items;
    }

    public void setItems(List<ReportOweFeeItemDto> items) {
        this.items = items;
    }

    public String[] getConfigIds() {
        return configIds;
    }

    public void setConfigIds(String[] configIds) {
        this.configIds = configIds;
    }

    public String getHasOweFee() {
        return hasOweFee;
    }

    public void setHasOweFee(String hasOweFee) {
        this.hasOweFee = hasOweFee;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRoomSubType() {
        return roomSubType;
    }

    public void setRoomSubType(String roomSubType) {
        this.roomSubType = roomSubType;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public double getTotalOweAmount() {
        return totalOweAmount;
    }

    public void setTotalOweAmount(double totalOweAmount) {
        this.totalOweAmount = totalOweAmount;
    }

    public List<ReportOweFeeItemDto> getItemTotalOweAmounts() {
        return itemTotalOweAmounts;
    }

    public void setItemTotalOweAmounts(List<ReportOweFeeItemDto> itemTotalOweAmounts) {
        this.itemTotalOweAmounts = itemTotalOweAmounts;
    }
}
