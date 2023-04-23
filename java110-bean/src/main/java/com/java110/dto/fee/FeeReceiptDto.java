package com.java110.dto.fee;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 收据数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeReceiptDto extends PageDto implements Serializable {

    private String amount;
    private String objId;
    private String remark;
    private String objName;
    private String carNum;
    private String roomName;
    private String communityId;
    private String receiptId;
    private String[] receiptIds;
    private String objType;
    private String feeTypeCd;
    private String feeName;
    private String payObjId;
    private String payObjName;
    private String qstartTime;
    private String qendTime;
    private String []detailIds;

    //商户名称
    private String storeName;

    private Date createTime;

    private String statusCd = "0";

    private String startTime;

    private String endTime;

    private String feeFlag;

    private String feeId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String[] getReceiptIds() {
        return receiptIds;
    }

    public void setReceiptIds(String[] receiptIds) {
        this.receiptIds = receiptIds;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getPayObjId() {
        return payObjId;
    }

    public void setPayObjId(String payObjId) {
        this.payObjId = payObjId;
    }

    public String getPayObjName() {
        return payObjName;
    }

    public void setPayObjName(String payObjName) {
        this.payObjName = payObjName;
    }

    public String getQstartTime() {
        return qstartTime;
    }

    public void setQstartTime(String qstartTime) {
        this.qstartTime = qstartTime;
    }

    public String getQendTime() {
        return qendTime;
    }

    public void setQendTime(String qendTime) {
        this.qendTime = qendTime;
    }

    public String[] getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(String[] detailIds) {
        this.detailIds = detailIds;
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

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
}
