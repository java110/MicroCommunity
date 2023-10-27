package com.java110.acct.payment;

import com.java110.dto.paymentPool.PaymentPoolDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.vo.ResultVo;

public interface IRefundMoneyAdapt {

    /**
     * 退款处理
     * @param onlinePayDto
     * @param paymentPoolDto
     * @return
     */
    ResultVo refund(OnlinePayDto onlinePayDto, PaymentPoolDto paymentPoolDto) throws Exception;
}
