package com.java110.vo.api;

import com.java110.vo.Vo;

/**
 * @ClassName ApiMainFeeVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/12 21:59
 * @Version 1.0
 * add by wuxw 2019/6/12
 **/
public class ApiMainFeeVo extends Vo {

    private String feeId;
    private String feeTypeCd;
    private String startTime;
    private String endTime;
    private String amount;

    private String squarePrice;
    private String additionalAmount;

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getSquarePrice() {
        return squarePrice;
    }

    public void setSquarePrice(String squarePrice) {
        this.squarePrice = squarePrice;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }
}
