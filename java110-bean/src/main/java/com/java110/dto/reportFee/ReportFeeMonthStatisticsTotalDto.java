package com.java110.dto.reportFee;

import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeConfigDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ReportFeeMonthStatisticsTotalDto
 * @Description 缴费明细报表大计、小计金额数据层封装
 * @Author fqz
 * @Date 2021/1/4 11:14
 * @Version 1.0
 * add by fqz 2021/1/4
 **/
public class ReportFeeMonthStatisticsTotalDto extends PageDto implements Serializable {
    //应收总金额(小计)
    private String totalReceivableAmount;

    //实收总金额(小计)
    private String totalReceivedAmount;

    //应收总金额(大计)
    private String allReceivableAmount;

    //实收总金额(大计)
    private String allReceivedAmount;

    //优惠金额(小计)
    private String totalPreferentialAmount;

    //减免金额(小计)
    private String totalDeductionAmount;

    //滞纳金(小计)
    private String totalLateFee;

    //空置房打折金额(小计)
    private String totalVacantHousingDiscount;

    //空置房减免金额(小计)
    private String totalVacantHousingReduction;

    //赠送规则金额(小计)
    private String totalGiftAmount;

    //无抵扣金额(小计)
    private String totalNoDeduction;
    //现金账户抵扣金额(小计)
    private String totalCashDeduction;
    //积分账户抵扣金额(小计)
    private String totalPointDeduction;
    //优惠券抵扣金额(小计)
    private String totalDiscountCouponDeduction;
    //优惠金额(大计)
    private String allPreferentialAmount;

    //减免金额(大计)
    private String allDeductionAmount;

    //滞纳金(大计)
    private String allLateFee;

    //空置房打折金额(大计)
    private String allVacantHousingDiscount;

    //空置房减免金额(大计)
    private String allVacantHousingReduction;

    //空置房减免金额(大计)
    private String allGiftAmount;

    //无抵扣(大计)
    private String allNoDeduction;
    //现金账户抵扣(大计)
    private String allCashDeduction;
    //积分账户抵扣(大计)
    private String allPointDeduction;
    //优惠卷抵扣(大计)
    private String allDiscountCouponDeduction;

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

    public String getTotalGiftAmount() {
        return totalGiftAmount;
    }

    public void setTotalGiftAmount(String totalGiftAmount) {
        this.totalGiftAmount = totalGiftAmount;
    }

    public String getAllGiftAmount() {
        return allGiftAmount;
    }

    public void setAllGiftAmount(String allGiftAmount) {
        this.allGiftAmount = allGiftAmount;
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
}
