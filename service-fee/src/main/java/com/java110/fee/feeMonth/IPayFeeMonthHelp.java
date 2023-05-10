package com.java110.fee.feeMonth;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;

public interface IPayFeeMonthHelp {

    PayFeeMonthOwnerDto generatorOwnerRoom(FeeDto feeDto);

    Double getMonthFeePrice(FeeDto feeDto);
}
