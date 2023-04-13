package com.java110.acct.payment;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.payment.NotifyPaymentOrderDto;
import com.java110.dto.payment.PaymentOrderDto;

import java.util.Map;

public interface IPaymentFactoryAdapt {
    /**
     * 支付
     * @param paymentOrderDto
     * @return
     */
    Map java110Payment(PaymentOrderDto paymentOrderDto, JSONObject reqJson,ICmdDataFlowContext context) throws Exception;

    /**
     * 支付完成通知
     * @param param
     * @return
     */
    PaymentOrderDto java110NotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto);
}
