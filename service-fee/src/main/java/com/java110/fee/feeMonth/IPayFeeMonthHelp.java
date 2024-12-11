package com.java110.fee.feeMonth;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeMonthOwnerDto;

import java.util.Date;

public interface IPayFeeMonthHelp {

    PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto);

    Double getMonthFeePrice(FeeDto feeDto);



    Double getDiscountAmount(Double feePrice, double receivedAmount, Date curDate, FeeDto feeDto);


    /**
     * 处理实收 问题
     * @param feeDto
     * @param payFeeMonthOwnerDto
     */
    void waitDispersedFeeDetail(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto,Double feePrice);

    /**
     * 处理欠费问题
     * @param feeDto
     * @param payFeeMonthOwnerDto
     * @param feePrice
     * @param deadlineTime
     */
    void waitDispersedOweFee(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto, Double feePrice, Date deadlineTime);
}
