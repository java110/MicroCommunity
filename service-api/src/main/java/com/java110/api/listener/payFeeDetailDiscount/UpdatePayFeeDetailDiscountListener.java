package com.java110.api.listener.payFeeDetailDiscount;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.payFeeDetailDiscount.IPayFeeDetailDiscountBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePayFeeDetailDiscountConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * 保存缴费优惠侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updatePayFeeDetailDiscountListener")
public class UpdatePayFeeDetailDiscountListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPayFeeDetailDiscountBMO payFeeDetailDiscountBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailDiscountId", "detailDiscountId不能为空");
        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "discountId", "请求报文中未包含discountId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        payFeeDetailDiscountBMOImpl.updatePayFeeDetailDiscount(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePayFeeDetailDiscountConstant.UPDATE_PAYFEEDETAILDISCOUNT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
