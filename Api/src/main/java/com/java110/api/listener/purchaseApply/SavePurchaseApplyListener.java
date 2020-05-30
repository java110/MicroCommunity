package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.purchaseApply.IPurchaseApplyBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.purchaseApplyUser.IPurchaseApplyUserInnerServiceSMO;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("savePurchaseApplyListener")
public class SavePurchaseApplyListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPurchaseApplyBMO purchaseApplyBMOImpl;
    @Autowired
    private IPurchaseApplyUserInnerServiceSMO iPurchaseApplyUserInnerServiceSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        reqJson.put("state", "1000");
        reqJson.put("createTime", DateUtil.getCurrentDate());
        reqJson.put("applyOrderId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));

        purchaseApplyBMOImpl.addPurchaseApply(reqJson, context);

        commit(context);

        ResponseEntity<String> responseEntity = context.getResponseEntity();

        //开始流程
        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);
            purchaseApplyDto.setCurrentUserId(reqJson.getString("userId"));
            iPurchaseApplyUserInnerServiceSMO.startProcess(purchaseApplyDto);
        }
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyConstant.ADD_PURCHASE_APPLY;
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
