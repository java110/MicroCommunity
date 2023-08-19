package com.java110.dto.oweFeeCallable;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 催缴记录数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OweFeeCallableDto extends PageDto implements Serializable {

    public static final String STATE_WAIT = "W";//状态 W 带催缴 C 催缴完成 F催缴失败 D 催缴中
    public static final String STATE_COMPLETE = "C";//状态 W 带催缴 C 催缴完成 F催缴失败 D 催缴中
    public static final String STATE_FAIL = "F";//状态 W 带催缴 C 催缴完成 F催缴失败 D 催缴中
    public static final String STATE_DOING = "D";//状态 W 带催缴 C 催缴完成 F催缴失败 D 催缴中

    public static final String CALLABLE_WAY_WECHAT = "WECHAT";//状态 W 带催缴 C 催缴完成 F催缴失败 D 催缴中
    public static final String CALLABLE_WAY_SMS = "SMS";//催缴方式： WECHAT 微信 SMS 短信 PRINT 上门催缴
    public static final String CALLABLE_WAY_PRINT = "PRINT";//催缴方式： WECHAT 微信 SMS 短信 PRINT 上门催缴


    private String remark;
    private String ofcId;
    private String ownerId;
    private String callableWay;
    private String callableWayName;
    private String feeId;
    private String payerObjName;
    private String ownerName;
    private String configId;
    private String feeName;
    private String staffName;
    private String amountdOwed;
    private String state;
    private String stateName;
    private String communityId;
    private String payerObjType;
    private String staffId;
    private String payerObjId;


    private Date createTime;

    private String statusCd = "0";

    private String startTime;
    private String endTime;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOfcId() {
        return ofcId;
    }

    public void setOfcId(String ofcId) {
        this.ofcId = ofcId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCallableWay() {
        return callableWay;
    }

    public void setCallableWay(String callableWay) {
        this.callableWay = callableWay;
    }

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getAmountdOwed() {
        return amountdOwed;
    }

    public void setAmountdOwed(String amountdOwed) {
        this.amountdOwed = amountdOwed;
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

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getCallableWayName() {
        return callableWayName;
    }

    public void setCallableWayName(String callableWayName) {
        this.callableWayName = callableWayName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
