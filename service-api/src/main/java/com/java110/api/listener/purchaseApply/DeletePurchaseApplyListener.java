package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.purchaseApply.IPurchaseApplyBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 删除采购/出库订单
 * add by zcc 2020/04/01
 */
@Java110Listener("deletePurchaseApplyListener")
public class DeletePurchaseApplyListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPurchaseApplyBMO purchaseApplyBMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单号不能为空");
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        if(!"1000".equals(purchaseApplyDtos.get(0).getState())){
            throw new IllegalArgumentException("只能取消未审核的订单");
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");

        deletePurchaseApply(reqJson, context);
        deletePurchaseApplyDetail(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyConstant.DELETE_PURCHASE_APPLY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    /**
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private void deletePurchaseApply(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyPo.class);
        super.delete(dataFlowContext, purchaseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY);
    }

    //删除订单明细
    private void deletePurchaseApplyDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        PurchaseApplyDetailPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyDetailPo.class);
        super.delete(dataFlowContext, purchaseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PURCHASE_APPLY_DETAIL);
    }

}
