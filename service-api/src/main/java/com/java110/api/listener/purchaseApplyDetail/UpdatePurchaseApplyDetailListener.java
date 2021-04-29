package com.java110.api.listener.purchaseApplyDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.purchaseApplyDetail.IPurchaseApplyDetailBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePurchaseApplyDetailConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;


/**
 * 保存订单明细侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updatePurchaseApplyDetailListener")
public class UpdatePurchaseApplyDetailListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPurchaseApplyDetailBMO purchaseApplyDetailBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "id", "id不能为空");
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "请求报文中未包含applyOrderId");
        Assert.hasKeyAndValue(reqJson, "resId", "请求报文中未包含resId");
        Assert.hasKeyAndValue(reqJson, "quantity", "请求报文中未包含quantity");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        purchaseApplyDetailBMOImpl.updatePurchaseApplyDetail(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyDetailConstant.UPDATE_PURCHASEAPPLYDETAIL;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
