package com.java110.dto.fee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 缴费记录转换为
 */
public class MonthFeeDetailDto implements Serializable {


    public MonthFeeDetailDto(double receivedAmount, FeeDetailDto feeDetailDto) {
        this.receivedAmount = receivedAmount;

        this.feeDetailDtos = new ArrayList<>();
        this.feeDetailDtos.add(feeDetailDto);
    }

    public MonthFeeDetailDto() {
    }

    private double receivedAmount;

    private List<FeeDetailDto> feeDetailDtos;

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public List<FeeDetailDto> getFeeDetailDtos() {
        return feeDetailDtos;
    }

    public void setFeeDetailDtos(List<FeeDetailDto> feeDetailDtos) {
        this.feeDetailDtos = feeDetailDtos;
    }
}
