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
public class ReportFeeMonthStatisticsDto extends PageDto implements Serializable {

    private String receivableAmount;
    private String statisticsId;
    private String updateTime;
    private String remark;
    private String objName;
    private String objNameNum;
    private String receivedAmount;
    private String payableAmount;
    private String feeYear;
    private String feeMonth;
    private String feeId;
    private String configId;
    private String objId;
    private String feeName;
    private String oweAmount;
    private String curOweAmount;
    private String communityId;
    private String feeCreateTime;
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

    private String roomName;
    private String carNum;
    private String psName;
    private String contractCode;
    private String payerObjType;
    private String payerObjId;
    private String payerObjName;


    private String ownerName;
    private String ownerId;
    private String detailId;
    private String builtUpArea;

    private String objCount;
    private String normalCount;

    private Date createTime;
    private String startTime;
    private String endTime;


    private String statusCd = "0";

    private int oweDay;
    private String deadlineTime;

    private String curMaxTime;
    private String importFeeName;

    private String oId;

    private String detailState;
    private String detailStateName;

    private String noDeduction; //无抵扣
    private String cashDeduction; //现金账户抵扣
    private String pointDeduction; //积分账户抵扣
    private String discountCouponDeduction; //优惠卷抵扣

    //支付方式
    private String primeRate;

    private double configReceivedAmount = 0;

    //应收总金额(小计)
    private String totalReceivableAmount;

    //实收总金额(小计)
    private String totalReceivedAmount;

    //无抵扣金额(小计)
    private String totalNoDeduction;
    //现金账户抵扣金额(小计)
    private String totalCashDeduction;
    //积分账户抵扣金额(小计)
    private String totalPointDeduction;
    //优惠券抵扣金额(小计)
    private String totalDiscountCouponDeduction;

    //应收总金额(大计)
    private String allReceivableAmount;

    //实收总金额(大计)
    private String allReceivedAmount;

    //实收总金额(大计)
    private String allOweAmount;

    //无抵扣(大计)
    private String allNoDeduction;
    //现金账户抵扣(大计)
    private String allCashDeduction;
    //积分账户抵扣(大计)
    private String allPointDeduction;
    //优惠卷抵扣(大计)
    private String allDiscountCouponDeduction;

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

    //账户抵扣金额
    private String withholdAmount;

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

    private String cashierId;
    private String cashierName;

    private String feeFlag;

    private String preOweAmount;

    private String ruleNameOne;
    private String ruleNameTwo;
    private String ruleNameThree;
    private String ruleNameFour;
    private String ruleNameFive;
    private String ruleNameSix;

    private String discountPriceOne;
    private String discountPriceTwo;
    private String discountPriceThree;
    private String discountPriceFour;
    private String discountPriceFive;
    private String discountPriceSix;

    private String discountSmallTypeOne;
    private String discountSmallTypeTwo;
    private String discountSmallTypeThree;
    private String discountSmallTypeFour;
    private String discountSmallTypeFive;
    private String discountSmallTypeSix;

    private String fadState;
    private String fadAmount;

    public String getNoDeduction() {
        return noDeduction;
    }

    public void setNoDeduction(String noDeduction) {
        this.noDeduction = noDeduction;
    }

    public String getCashDeduction() {
        return cashDeduction;
    }

    public void setCashDeduction(String cashDeduction) {
        this.cashDeduction = cashDeduction;
    }

    public String getPointDeduction() {
        return pointDeduction;
    }

    public void setPointDeduction(String pointDeduction) {
        this.pointDeduction = pointDeduction;
    }

    public String getDiscountCouponDeduction() {
        return discountCouponDeduction;
    }

    public void setDiscountCouponDeduction(String discountCouponDeduction) {
        this.discountCouponDeduction = discountCouponDeduction;
    }

    private String acctAmount;
    private String discountAmount;
    private String lateAmount;

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
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

    public String getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(String feeYear) {
        this.feeYear = feeYear;
    }

    public String getFeeMonth() {
        return feeMonth;
    }

    public void setFeeMonth(String feeMonth) {
        this.feeMonth = feeMonth;
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

    public String getFeeCreateTime() {
        return feeCreateTime;
    }

    public void setFeeCreateTime(String feeCreateTime) {
        this.feeCreateTime = feeCreateTime;
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

    public String getPayerObjType() {
        return payerObjType;
    }

    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
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

    public String getWithholdAmount() {
        return withholdAmount;
    }

    public void setWithholdAmount(String withholdAmount) {
        this.withholdAmount = withholdAmount;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getFeeFlag() {
        return feeFlag;
    }

    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    public String getPreOweAmount() {
        return preOweAmount;
    }

    public void setPreOweAmount(String preOweAmount) {
        this.preOweAmount = preOweAmount;
    }

    public String getRuleNameOne() {
        return ruleNameOne;
    }

    public void setRuleNameOne(String ruleNameOne) {
        this.ruleNameOne = ruleNameOne;
    }

    public String getRuleNameTwo() {
        return ruleNameTwo;
    }

    public void setRuleNameTwo(String ruleNameTwo) {
        this.ruleNameTwo = ruleNameTwo;
    }

    public String getRuleNameThree() {
        return ruleNameThree;
    }

    public void setRuleNameThree(String ruleNameThree) {
        this.ruleNameThree = ruleNameThree;
    }

    public String getRuleNameFour() {
        return ruleNameFour;
    }

    public void setRuleNameFour(String ruleNameFour) {
        this.ruleNameFour = ruleNameFour;
    }

    public String getRuleNameFive() {
        return ruleNameFive;
    }

    public void setRuleNameFive(String ruleNameFive) {
        this.ruleNameFive = ruleNameFive;
    }

    public String getRuleNameSix() {
        return ruleNameSix;
    }

    public void setRuleNameSix(String ruleNameSix) {
        this.ruleNameSix = ruleNameSix;
    }

    public String getDiscountPriceOne() {
        return discountPriceOne;
    }

    public void setDiscountPriceOne(String discountPriceOne) {
        this.discountPriceOne = discountPriceOne;
    }

    public String getDiscountPriceTwo() {
        return discountPriceTwo;
    }

    public void setDiscountPriceTwo(String discountPriceTwo) {
        this.discountPriceTwo = discountPriceTwo;
    }

    public String getDiscountPriceThree() {
        return discountPriceThree;
    }

    public void setDiscountPriceThree(String discountPriceThree) {
        this.discountPriceThree = discountPriceThree;
    }

    public String getDiscountPriceFour() {
        return discountPriceFour;
    }

    public void setDiscountPriceFour(String discountPriceFour) {
        this.discountPriceFour = discountPriceFour;
    }

    public String getDiscountPriceFive() {
        return discountPriceFive;
    }

    public void setDiscountPriceFive(String discountPriceFive) {
        this.discountPriceFive = discountPriceFive;
    }

    public String getDiscountPriceSix() {
        return discountPriceSix;
    }

    public void setDiscountPriceSix(String discountPriceSix) {
        this.discountPriceSix = discountPriceSix;
    }

    public String getDiscountSmallTypeOne() {
        return discountSmallTypeOne;
    }

    public void setDiscountSmallTypeOne(String discountSmallTypeOne) {
        this.discountSmallTypeOne = discountSmallTypeOne;
    }

    public String getDiscountSmallTypeTwo() {
        return discountSmallTypeTwo;
    }

    public void setDiscountSmallTypeTwo(String discountSmallTypeTwo) {
        this.discountSmallTypeTwo = discountSmallTypeTwo;
    }

    public String getDiscountSmallTypeThree() {
        return discountSmallTypeThree;
    }

    public void setDiscountSmallTypeThree(String discountSmallTypeThree) {
        this.discountSmallTypeThree = discountSmallTypeThree;
    }

    public String getDiscountSmallTypeFour() {
        return discountSmallTypeFour;
    }

    public void setDiscountSmallTypeFour(String discountSmallTypeFour) {
        this.discountSmallTypeFour = discountSmallTypeFour;
    }

    public String getDiscountSmallTypeFive() {
        return discountSmallTypeFive;
    }

    public void setDiscountSmallTypeFive(String discountSmallTypeFive) {
        this.discountSmallTypeFive = discountSmallTypeFive;
    }

    public String getDiscountSmallTypeSix() {
        return discountSmallTypeSix;
    }

    public void setDiscountSmallTypeSix(String discountSmallTypeSix) {
        this.discountSmallTypeSix = discountSmallTypeSix;
    }


    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState;
    }

    public String getDetailStateName() {
        return detailStateName;
    }

    public void setDetailStateName(String detailStateName) {
        this.detailStateName = detailStateName;
    }

    public String getTotalNoDeduction() {
        return totalNoDeduction;
    }

    public void setTotalNoDeduction(String totalNoDeduction) {
        this.totalNoDeduction = totalNoDeduction;
    }

    public String getTotalCashDeduction() {
        return totalCashDeduction;
    }

    public void setTotalCashDeduction(String totalCashDeduction) {
        this.totalCashDeduction = totalCashDeduction;
    }

    public String getTotalPointDeduction() {
        return totalPointDeduction;
    }

    public void setTotalPointDeduction(String totalPointDeduction) {
        this.totalPointDeduction = totalPointDeduction;
    }

    public String getTotalDiscountCouponDeduction() {
        return totalDiscountCouponDeduction;
    }

    public void setTotalDiscountCouponDeduction(String totalDiscountCouponDeduction) {
        this.totalDiscountCouponDeduction = totalDiscountCouponDeduction;
    }

    public String getAllNoDeduction() {
        return allNoDeduction;
    }

    public void setAllNoDeduction(String allNoDeduction) {
        this.allNoDeduction = allNoDeduction;
    }

    public String getAllCashDeduction() {
        return allCashDeduction;
    }

    public void setAllCashDeduction(String allCashDeduction) {
        this.allCashDeduction = allCashDeduction;
    }

    public String getAllPointDeduction() {
        return allPointDeduction;
    }

    public void setAllPointDeduction(String allPointDeduction) {
        this.allPointDeduction = allPointDeduction;
    }

    public String getAllDiscountCouponDeduction() {
        return allDiscountCouponDeduction;
    }

    public void setAllDiscountCouponDeduction(String allDiscountCouponDeduction) {
        this.allDiscountCouponDeduction = allDiscountCouponDeduction;
    }

    public String getFadState() {
        return fadState;
    }

    public void setFadState(String fadState) {
        this.fadState = fadState;
    }

    public String getFadAmount() {
        return fadAmount;
    }

    public void setFadAmount(String fadAmount){
            this.fadAmount = fadAmount;
    }
    public String getAcctAmount() {
        return acctAmount;
    }

    public void setAcctAmount(String acctAmount) {
        this.acctAmount = acctAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(String lateAmount) {
        this.lateAmount = lateAmount;
    }

    public String getPayerObjName() {
        return payerObjName;
    }

    public void setPayerObjName(String payerObjName) {
        this.payerObjName = payerObjName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
