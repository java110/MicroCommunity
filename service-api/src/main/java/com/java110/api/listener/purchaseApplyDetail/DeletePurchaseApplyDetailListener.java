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
 * 删除订单明细(采购/出库明细订单)
 * add by zcc 2020-04-09
 */
@Java110Listener("deletePurchaseApplyDetailListener")
public class DeletePurchaseApplyDetailListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPurchaseApplyDetailBMO purchaseApplyDetailBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单号applyOrderId不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        purchaseApplyDetailBMOImpl.deletePurchaseApplyDetail(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyDetailConstant.DELETE_PURCHASEAPPLYDETAIL;
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
