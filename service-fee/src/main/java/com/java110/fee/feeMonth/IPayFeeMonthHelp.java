package com.java110.fee.feeMonth;

import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;

import java.util.Date;
import java.util.List;

public interface IPayFeeMonthHelp {

    PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto);

    Double getMonthFeePrice(FeeDto feeDto);


    Double getReceivableAmount(List<FeeDetailDto> feeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto);

    /**
     * 计算实收
     * @param feeDetailDtos
     * @param feePrice
     * @return
     */
    Double getReceivedAmount(List<FeeDetailDto> feeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto);

    Double getDiscountAmount(Double feePrice, double parseDouble, Date curDate, FeeDto feeDto);

    /**
     * 计算缴费DetailId
     * @param feeDetailDtos
     * @param time
     * @return
     */
    String getFeeDetailId(List<FeeDetailDto> feeDetailDtos, Date time);

}
