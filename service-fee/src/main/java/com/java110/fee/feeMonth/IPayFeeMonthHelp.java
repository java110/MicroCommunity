package com.java110.fee.feeMonth;

import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.MonthFeeDetailDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPayFeeMonthHelp {

    PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto);

    Double getMonthFeePrice(FeeDto feeDto);


    Double getReceivableAmount(List<FeeDetailDto> feeDetailDtos,Map<String, MonthFeeDetailDto> monthFeeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto);

    /**
     * 计算实收
     * @param feeDetailDtos
     * @param feePrice
     * @return
     */
    Double getReceivedAmount(List<FeeDetailDto> feeDetailDtos,Map<String ,MonthFeeDetailDto> monthFeeDetailDtos, Double feePrice, Date curDate, FeeDto feeDto);

    Double getDiscountAmount(Double feePrice, double receivedAmount, Date curDate, FeeDto feeDto);

    /**
     * 计算缴费DetailId
     * @param feeDetailDtos
     * @param time
     * @return
     */
    String getFeeDetailId(List<FeeDetailDto> feeDetailDtos, Date time);

    /**
     * 获取 支付时间
     * @param feeDetailDtos
     * @param detailId
     * @return
     */
    String getFeeFeeTime(List<FeeDetailDto> feeDetailDtos, String detailId);

    /**
     * 缴费记录转换为月缴费记录，金额 除以 缴费时间段内所包含的月个数
     * @param feeDetailDtos
     * @return
     */
    Map<String ,MonthFeeDetailDto> analysisMonthFeeDetail(List<FeeDetailDto> feeDetailDtos);
}
