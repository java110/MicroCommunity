package com.java110.acct.payment.business.goods;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.payment.PaymentOrderDto;
import org.springframework.stereotype.Service;

@Service("buyGroupGoods")
public class BuyGroupGoodsBusiness implements IPaymentBusiness {
    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");

        /**
         * ?page=1&row=1&shopId=502022081626050001&cartType=3306
         * &productId=902022120101840004&valueId=912022120139430012&goodsNum=1&groupId=102022122018530001
         */

        JSONObject paramIn = new JSONObject();
        paramIn.put("page",1);
        paramIn.put("row",1);
        paramIn.put("shopId",reqJson.getString("shopId"));
        paramIn.put("cartType","3306");
        paramIn.put("productId",reqJson.getString("productId"));
        paramIn.put("valueId",reqJson.getString("valueId"));
        paramIn.put("goodsNum",reqJson.getString("goodsNum"));
        paramIn.put("groupId",reqJson.getString("groupId"));
        JSONObject orderInfo = CallApiServiceFactory.getForApi(appId, null, "storeOrder.computeGroupProductPrice"+CallApiServiceFactory.mapToUrlParam(paramIn),
                JSONObject.class);
        String orderId = GenerateCodeFactory.getGeneratorId("11");
        String feeName = "购买商品";
        double money = Double.parseDouble(orderInfo.getString("payPrice"));

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(orderId);
        paymentOrderDto.setMoney(money);
        paymentOrderDto.setName(feeName);
        reqJson.put("payAmount",money+"");
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        reqJson.put("orderId", paymentOrderDto.getOrderId());
        reqJson.put("payOrderId",paymentOrderDto.getTransactionId());
        JSONObject paramOut = CallApiServiceFactory.postForApi(paymentOrderDto.getAppId(), reqJson, "cart.unifiedGroupProductOrder", JSONObject.class, paymentOrderDto.getUserId());

    }
}
