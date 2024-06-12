package com.java110.acct.integral;

import com.java110.dto.integral.GiftIntegralDto;

public interface IComputeGiftIntegral {

    /**
     * 赠送积分和金额计算
     *
     * @param payMoney
     * @param month
     * @param communityId
     * @return
     */
    GiftIntegralDto gift(double payMoney, int month, String communityId);
}
