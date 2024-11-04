package com.java110.dto.reportFee;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 费用月统计数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportFeeMonthStatisticsPrepaymentDto extends PageDto implements Serializable {
    private String receivableAmount;
    private String prepaymentId;
    private String updateTime;
    private String remark;
    private String objName;
    private String objNameNum;
    private String receivedAmount;
    private String payableAmount;
    private String feeId;
    private String configId;
    private String objId;
    private String feeName;
    private String oweAmount;
    private String curOweAmount;
    private String communityId;
    private String feeStartTime;
    private String feeEndTime;
    private String objType;
    private String floorId;
    private String floorNum;
    private String unitId;
    private String[] unitIds;
    private String unitNum;
    private String roomId;
    private String roomNum;
    private String carNum;
    private String ownerName;
    private String ownerId;
    private String detailId;
    private String builtUpArea;

    private String objCount;
    private String normalCount;

    private Date createTime;
    private String startTime;
    private String endTime;
    private String startBeginTime;
    private String startFinishTime;
    private String endBeginTime;
    private String endFinishTime;

    private String statusCd = "0";

    private int oweDay;
    private String deadlineTime;

    private String curMaxTime;
    private String importFeeName;

    private String oId;

    //支付方式
    private String primeRate;

    private double configReceivedAmount = 0;

    //应收总金额(小计)
    private String totalReceivableAmount;

    //实收总金额(小计)
    private String totalReceivedAmount;

    //应缴金额(小计)
    private String totalPayableAmount;

    //欠费金额(小计)
    private String totalOweAmount;

    //优惠金额(小计)
    private String totalPreferentialAmount;

    //减免金额(小计)
    private String totalDeductionAmount;

    //滞纳金(小计)
    private String totalLateFee;

    //空置房打折(小计)
    private String totalVacantHousingDiscount;

    //空置房减免(小计)
    private String totalVacantHousingReduction;

    //赠送规则金额(小计)
    private String totalGiftAmount;

    //应收总金额(大计)
    private String allReceivableAmount;

    //实收总金额(大计)
    private String allReceivedAmount;

    //实收总金额(大计)
    private String allOweAmount;

    //应缴金额(大计)
    private String allPayableAmount;

    //优惠金额(小计)
    private String allPreferentialAmount;

    //减免金额(小计)
    private String allDeductionAmount;

    //滞纳金(小计)
    private String allLateFee;

    //空置房打折(小计)
    private String allVacantHousingDiscount;

    //空置房减免(小计)
    private String allVacantHousingReduction;

    //赠送规则金额(小计)
    private String allGiftAmount;

    private List<FeeConfigDto> FeeConfigDtos;

    //费用类型
    private String feeTypeCd;

    //费用类型名称
    private String feeTypeCdName;

    //打折金额（包括打折、减免、滞纳金、空置房打折、空置房减免金额等）
    private String discountPrice;

    //1:打折 2:减免 3:滞纳金 4:空置房打折 5:空置房减免
    private String discountSmallType;

    //优惠金额
    private String preferentialAmount;

    //减免金额
    private String deductionAmount;

    //滞纳金
    private String lateFee;

    //空置房打折金额
    private String vacantHousingDiscount;

    //空置房减免金额
    private String vacantHousingReduction;

    //赠送金额
    private String giftAmount;

    //收费率
    private String chargeRate;

    //状态
    private String state;

    //状态名称
    private String stateName;

    private String repairId;

    private double hisOweAmount;
    private double curReceivableAmount;
    private double curReceivedAmount;
    private double hisOweReceivedAmount;
    private double preReceivedAmount;
    private double allHisOweReceivedAmount;

    private String[] configIds;

    private String yearMonth;

    private String payTime;
    private String prepaymentState;
    private String prepaymentStateName;
    private String billState;
    private String billStateName;
    private String prepaymentReceivableAmount;
    private String prepaymentReceivedAmount;

    private String feeBeginTime;
    private String feeFinishTime;
    private String prepaymentCycle;
    private String payFeeFlag;
    private String prepaymentDetailId;

    private String nextStartTime;

    private String newBeginTime;
    private String newFinishTime;

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getPrepaymentId() {
        return prepaymentId;
    }

    public void setPrepaymentId(String prepaymentId) {
        this.prepaymentId = prepaymentId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getOweAmount() {
        return oweAmount;
    }

    public void setOweAmount(String oweAmount) {
        this.oweAmount = oweAmount;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getFeeStartTime() {
        return feeStartTime;
    }

    public void setFeeStartTime(String feeStartTime) {
        this.feeStartTime = feeStartTime;
    }

    public String getFeeEndTime() {
        return feeEndTime;
    }

    public void setFeeEndTime(String feeEndTime) {
        this.feeEndTime = feeEndTime;
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

    public int getOweDay() {
        return oweDay;
    }

    public void setOweDay(int oweDay) {
        this.oweDay = oweDay;
    }

    public String getObjCount() {
        return objCount;
    }

    public void setObjCount(String objCount) {
        this.objCount = objCount;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getImportFeeName() {
        return importFeeName;
    }

    public void setImportFeeName(String importFeeName) {
        this.importFeeName = importFeeName;
    }

    public String getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(String normalCount) {
        this.normalCount = normalCount;
    }

    public String getTotalReceivableAmount() {
        return totalReceivableAmount;
    }

    public void setTotalReceivableAmount(String totalReceivableAmount) {
        this.totalReceivableAmount = totalReceivableAmount;
    }

    public String getTotalReceivedAmount() {
        return totalReceivedAmount;
    }

    public void setTotalReceivedAmount(String totalReceivedAmount) {
        this.totalReceivedAmount = totalReceivedAmount;
    }

    public String getAllReceivableAmount() {
        return allReceivableAmount;
    }

    public void setAllReceivableAmount(String allReceivableAmount) {
        this.allReceivableAmount = allReceivableAmount;
    }

    public String getAllReceivedAmount() {
        return allReceivedAmount;
    }

    public void setAllReceivedAmount(String allReceivedAmount) {
        this.allReceivedAmount = allReceivedAmount;
    }

    public List<FeeConfigDto> getFeeConfigDtos() {
        return FeeConfigDtos;
    }

    public void setFeeConfigDtos(List<FeeConfigDto> feeConfigDtoS) {
        FeeConfigDtos = feeConfigDtoS;
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

    public String getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(String preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }

    public String getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(String deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getDiscountSmallType() {
        return discountSmallType;
    }

    public void setDiscountSmallType(String discountSmallType) {
        this.discountSmallType = discountSmallType;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getVacantHousingDiscount() {
        return vacantHousingDiscount;
    }

    public void setVacantHousingDiscount(String vacantHousingDiscount) {
        this.vacantHousingDiscount = vacantHousingDiscount;
    }

    public String getVacantHousingReduction() {
        return vacantHousingReduction;
    }

    public void setVacantHousingReduction(String vacantHousingReduction) {
        this.vacantHousingReduction = vacantHousingReduction;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getCurMaxTime() {
        return curMaxTime;
    }

    public void setCurMaxTime(String curMaxTime) {
        this.curMaxTime = curMaxTime;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getFeeTypeCdName() {
        return feeTypeCdName;
    }

    public void setFeeTypeCdName(String feeTypeCdName) {
        this.feeTypeCdName = feeTypeCdName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getAllOweAmount() {
        return allOweAmount;
    }

    public void setAllOweAmount(String allOweAmount) {
        this.allOweAmount = allOweAmount;
    }

    public String getCurOweAmount() {
        return curOweAmount;
    }

    public void setCurOweAmount(String curOweAmount) {
        this.curOweAmount = curOweAmount;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String[] getConfigIds() {
        return configIds;
    }

    public void setConfigIds(String[] configIds) {
        this.configIds = configIds;
    }

    public double getConfigReceivedAmount() {
        return configReceivedAmount;
    }

    public void setConfigReceivedAmount(double configReceivedAmount) {
        this.configReceivedAmount = configReceivedAmount;
    }

    public double getHisOweAmount() {
        return hisOweAmount;
    }

    public void setHisOweAmount(double hisOweAmount) {
        this.hisOweAmount = hisOweAmount;
    }

    public double getCurReceivableAmount() {
        return curReceivableAmount;
    }

    public void setCurReceivableAmount(double curReceivableAmount) {
        this.curReceivableAmount = curReceivableAmount;
    }

    public double getCurReceivedAmount() {
        return curReceivedAmount;
    }

    public void setCurReceivedAmount(double curReceivedAmount) {
        this.curReceivedAmount = curReceivedAmount;
    }

    public double getHisOweReceivedAmount() {
        return hisOweReceivedAmount;
    }

    public void setHisOweReceivedAmount(double hisOweReceivedAmount) {
        this.hisOweReceivedAmount = hisOweReceivedAmount;
    }

    public double getPreReceivedAmount() {
        return preReceivedAmount;
    }

    public void setPreReceivedAmount(double preReceivedAmount) {
        this.preReceivedAmount = preReceivedAmount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public double getAllHisOweReceivedAmount() {
        return allHisOweReceivedAmount;
    }

    public void setAllHisOweReceivedAmount(double allHisOweReceivedAmount) {
        this.allHisOweReceivedAmount = allHisOweReceivedAmount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getObjNameNum() {
        return objNameNum;
    }

    public void setObjNameNum(String objNameNum) {
        this.objNameNum = objNameNum;
    }

    public String getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(String giftAmount) {
        this.giftAmount = giftAmount;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String[] getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(String[] unitIds) {
        this.unitIds = unitIds;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPrepaymentState() {
        return prepaymentState;
    }

    public void setPrepaymentState(String prepaymentState) {
        this.prepaymentState = prepaymentState;
    }

    public String getFeeBeginTime() {
        return feeBeginTime;
    }

    public void setFeeBeginTime(String feeBeginTime) {
        this.feeBeginTime = feeBeginTime;
    }

    public String getFeeFinishTime() {
        return feeFinishTime;
    }

    public void setFeeFinishTime(String feeFinishTime) {
        this.feeFinishTime = feeFinishTime;
    }

    public String getPrepaymentCycle() {
        return prepaymentCycle;
    }

    public void setPrepaymentCycle(String prepaymentCycle) {
        this.prepaymentCycle = prepaymentCycle;
    }

    public String getPayFeeFlag() {
        return payFeeFlag;
    }

    public void setPayFeeFlag(String payFeeFlag) {
        this.payFeeFlag = payFeeFlag;
    }

    public String getPrepaymentDetailId() {
        return prepaymentDetailId;
    }

    public void setPrepaymentDetailId(String prepaymentDetailId) {
        this.prepaymentDetailId = prepaymentDetailId;
    }

    public String getPrepaymentStateName() {
        return prepaymentStateName;
    }

    public void setPrepaymentStateName(String prepaymentStateName) {
        this.prepaymentStateName = prepaymentStateName;
    }

    public String getPrepaymentReceivableAmount() {
        return prepaymentReceivableAmount;
    }

    public void setPrepaymentReceivableAmount(String prepaymentReceivableAmount) {
        this.prepaymentReceivableAmount = prepaymentReceivableAmount;
    }

    public String getPrepaymentReceivedAmount() {
        return prepaymentReceivedAmount;
    }

    public void setPrepaymentReceivedAmount(String prepaymentReceivedAmount) {
        this.prepaymentReceivedAmount = prepaymentReceivedAmount;
    }

    public String getStartBeginTime() {
        return startBeginTime;
    }

    public void setStartBeginTime(String startBeginTime) {
        this.startBeginTime = startBeginTime;
    }

    public String getStartFinishTime() {
        return startFinishTime;
    }

    public void setStartFinishTime(String startFinishTime) {
        this.startFinishTime = startFinishTime;
    }

    public String getEndBeginTime() {
        return endBeginTime;
    }

    public void setEndBeginTime(String endBeginTime) {
        this.endBeginTime = endBeginTime;
    }

    public String getEndFinishTime() {
        return endFinishTime;
    }

    public void setEndFinishTime(String endFinishTime) {
        this.endFinishTime = endFinishTime;
    }

    public String getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(String totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public String getAllPayableAmount() {
        return allPayableAmount;
    }

    public void setAllPayableAmount(String allPayableAmount) {
        this.allPayableAmount = allPayableAmount;
    }

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public String getNewBeginTime() {
        return newBeginTime;
    }

    public void setNewBeginTime(String newBeginTime) {
        this.newBeginTime = newBeginTime;
    }

    public String getNewFinishTime() {
        return newFinishTime;
    }

    public void setNewFinishTime(String newFinishTime) {
        this.newFinishTime = newFinishTime;
    }

    public String getTotalOweAmount() {
        return totalOweAmount;
    }

    public void setTotalOweAmount(String totalOweAmount) {
        this.totalOweAmount = totalOweAmount;
    }

    public String getTotalPreferentialAmount() {
        return totalPreferentialAmount;
    }

    public void setTotalPreferentialAmount(String totalPreferentialAmount) {
        this.totalPreferentialAmount = totalPreferentialAmount;
    }

    public String getTotalDeductionAmount() {
        return totalDeductionAmount;
    }

    public void setTotalDeductionAmount(String totalDeductionAmount) {
        this.totalDeductionAmount = totalDeductionAmount;
    }

    public String getTotalLateFee() {
        return totalLateFee;
    }

    public void setTotalLateFee(String totalLateFee) {
        this.totalLateFee = totalLateFee;
    }

    public String getTotalVacantHousingDiscount() {
        return totalVacantHousingDiscount;
    }

    public void setTotalVacantHousingDiscount(String totalVacantHousingDiscount) {
        this.totalVacantHousingDiscount = totalVacantHousingDiscount;
    }

    public String getTotalVacantHousingReduction() {
        return totalVacantHousingReduction;
    }

    public void setTotalVacantHousingReduction(String totalVacantHousingReduction) {
        this.totalVacantHousingReduction = totalVacantHousingReduction;
    }

    public String getTotalGiftAmount() {
        return totalGiftAmount;
    }

    public void setTotalGiftAmount(String totalGiftAmount) {
        this.totalGiftAmount = totalGiftAmount;
    }

    public String getAllPreferentialAmount() {
        return allPreferentialAmount;
    }

    public void setAllPreferentialAmount(String allPreferentialAmount) {
        this.allPreferentialAmount = allPreferentialAmount;
    }

    public String getAllDeductionAmount() {
        return allDeductionAmount;
    }

    public void setAllDeductionAmount(String allDeductionAmount) {
        this.allDeductionAmount = allDeductionAmount;
    }

    public String getAllLateFee() {
        return allLateFee;
    }

    public void setAllLateFee(String allLateFee) {
        this.allLateFee = allLateFee;
    }

    public String getAllVacantHousingDiscount() {
        return allVacantHousingDiscount;
    }

    public void setAllVacantHousingDiscount(String allVacantHousingDiscount) {
        this.allVacantHousingDiscount = allVacantHousingDiscount;
    }

    public String getAllVacantHousingReduction() {
        return allVacantHousingReduction;
    }

    public void setAllVacantHousingReduction(String allVacantHousingReduction) {
        this.allVacantHousingReduction = allVacantHousingReduction;
    }

    public String getAllGiftAmount() {
        return allGiftAmount;
    }

    public void setAllGiftAmount(String allGiftAmount) {
        this.allGiftAmount = allGiftAmount;
    }

    public String getbillState() {
        return billState;
    }

    public void setbillState(String billState) {
        this.billState = billState;
    }

    public String getbillStateName() {
        return billStateName;
    }

    public void setbillStateName(String billStateName) {
        this.billStateName = billStateName;
    }
}
