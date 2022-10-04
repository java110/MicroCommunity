package com.java110.acct.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.payment.PaymentOrderDto;

/**
 * 业务统一下单接口
 */
public interface IPaymentBusiness {


    /**
     * 统一下单
     * @param reqJson
     * @return
     */
    PaymentOrderDto unified(ICmdDataFlowContext context,JSONObject reqJson);

    /**
     * 支付完通知接口
     * @param paymentOrderDto
     */
    void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson);
}
