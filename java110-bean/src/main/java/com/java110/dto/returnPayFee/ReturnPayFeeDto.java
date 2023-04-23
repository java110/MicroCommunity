package com.java110.dto.returnPayFee;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeAccountDetailDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 退费表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReturnPayFeeDto extends PageDto implements Serializable {

    public static final String STATE_WAIT = "1000";

    private String reason;
    private String primeRate;
    private String feeTypeCd;
    private String payTime;
    private String detailId;
    private String receivableAmount;
    private String cycles;
    private String remark;
    private String receivedAmount;
    private String feeId;
    private String returnFeeId;
    private String configId;
    private String state;
    private String communityId;

    private String createTime;
    private String startTime;
    private String endTime;

    private String statusCd = "0";
    private String feeTypeCdName;
    private String stateName;

    private String roomId;
    private String roomNum;
    private String unitId;
    private String unitNum;
    private String floorId;
    private String floorNum;

    private String carId;
    private String carNum;
    private String psId;
    private String psNum;
    private String paId;
    private String paNum;

    private String payerObjType;
    private String payerObjName;

    //alter table return_pay_fee add COLUMN apply_person_id varchar(30) comment '申请人ID';
    //alter table return_pay_fee add COLUMN apply_person_name varchar(30) comment '申请人';
    //
    //alter table return_pay_fee add COLUMN audit_person_id varchar(30) comment '审核人ID';
    //alter table return_pay_fee add COLUMN audit_person_name varchar(30) comment '审核人';
    private String applyPersonId;
    private String applyPersonName;
    private String auditPersonId;
    private String auditPersonName;

    private List<FeeAccountDetailDto> feeAccountDetailDtoList;
    private List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtoList;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrimeRate() {
        return primeRate;
    }

    public void setPrimeRate(String primeRate) {
        this.primeRate = primeRate;
    }

    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
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

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public String getReturnFeeId() {
        return returnFeeId;
    }

    public void setReturnFeeId(String returnFeeId) {
        this.returnFeeId = returnFeeId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getFeeTypeCdName() {
        return feeTypeCdName;
    }

    public void setFeeTypeCdName(String feeTypeCdName) {
        this.feeTypeCdName = feeTypeCdName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getPsNum() {
        return psNum;
    }

    public void setPsNum(String psNum) {
        this.psNum = psNum;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getPaNum() {
        return paNum;
    }

    public void setPaNum(String paNum) {
        this.paNum = paNum;
    }

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public List<FeeAccountDetailDto> getFeeAccountDetailDtoList() {
        return feeAccountDetailDtoList;
    }

    public void setFeeAccountDetailDtoList(List<FeeAccountDetailDto> feeAccountDetailDtoList) {
        this.feeAccountDetailDtoList = feeAccountDetailDtoList;
    }

    public List<PayFeeDetailDiscountDto> getPayFeeDetailDiscountDtoList() {
        return payFeeDetailDiscountDtoList;
    }

    public void setPayFeeDetailDiscountDtoList(List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtoList) {
        this.payFeeDetailDiscountDtoList = payFeeDetailDiscountDtoList;
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

    public String getApplyPersonId() {
        return applyPersonId;
    }

    public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getAuditPersonId() {
        return auditPersonId;
    }

    public void setAuditPersonId(String auditPersonId) {
        this.auditPersonId = auditPersonId;
    }

    public String getAuditPersonName() {
        return auditPersonName;
    }

    public void setAuditPersonName(String auditPersonName) {
        this.auditPersonName = auditPersonName;
    }
}
