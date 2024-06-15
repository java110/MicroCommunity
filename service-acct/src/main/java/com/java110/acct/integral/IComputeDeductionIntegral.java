package com.java110.acct.integral;

import com.java110.dto.integral.DeductionIntegralDto;
import com.java110.dto.integral.GiftIntegralDto;

public interface IComputeDeductionIntegral {
    /**
     * 抵扣积分和金额计算
     *
     * @param communityId
     * @return
     */
    DeductionIntegralDto deduction(String userId,String orderId, String communityId);
}
