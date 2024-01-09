package com.java110.acct.payment.business.payFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.MoneyUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * 房屋费 停车费缴费
 */
@Service("payFee")
public class PayFeePaymentBusiness implements IPaymentBusiness {


    private final static Logger logger = LoggerFactory.getLogger(PayFeePaymentBusiness.class);


    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        JSONObject orderInfo = CallApiServiceFactory.postForApi(appId, reqJson, "fee.payFeePre", JSONObject.class, userId);
        String orderId = orderInfo.getString("oId");
        String feeName = orderInfo.getString("feeName");
        double money = Double.parseDouble(orderInfo.getString("receivedAmount"));

        //这里防止 小数点不是 2位 比如 3位之类的 微信平台不支持
        money = MoneyUtil.computePriceScale(money,"1",2);


        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName);
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {


        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", paymentOrderDto.getOrderId());
        JSONObject paramOut = CallApiServiceFactory.postForApi(paymentOrderDto.getAppId(), paramIn, "fee.payFeeConfirm", JSONObject.class, "-1");

    }

}
