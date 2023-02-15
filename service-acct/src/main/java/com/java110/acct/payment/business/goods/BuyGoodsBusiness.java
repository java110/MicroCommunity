package com.java110.acct.payment.business.goods;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.dto.payment.PaymentOrderDto;
import org.springframework.stereotype.Service;

@Service("buyGoods")
public class BuyGoodsBusiness implements IPaymentBusiness {
    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");

        ///api/cart/unifiedOrder
        JSONObject orderInfo = CallApiServiceFactory.postForApi(appId, reqJson, "/cart/unifiedOrder", JSONObject.class, userId);
        String orderId = orderInfo.getString("orderId");
        String feeName = "购买商品";
        double money = Double.parseDouble(orderInfo.getString("payPrice"));

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        JSONObject paramIn = new JSONObject();
        paramIn.put("orderId", paymentOrderDto.getOrderId());
        paramIn.put("payOrderId",paymentOrderDto.getTransactionId());
        JSONObject paramOut = CallApiServiceFactory.postForApi(paymentOrderDto.getAppId(), reqJson, "/cart/payNotifyOrder", JSONObject.class, paymentOrderDto.getUserId());

    }
}
