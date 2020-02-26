package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyDataVo;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listPurchaseApplysListener")
public class ListPurchaseApplysListener extends AbstractServiceApiListener {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePurchaseApplyConstant.LIST_PURCHASE_APPLY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IPurchaseApplyInnerServiceSMO getPurchaseApplyInnerServiceSMOImpl() {
        return purchaseApplyInnerServiceSMOImpl;
    }

    public void setPurchaseApplyInnerServiceSMOImpl(IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl) {
        this.purchaseApplyInnerServiceSMOImpl = purchaseApplyInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);

        int count = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplysCount(purchaseApplyDto);

        List<ApiPurchaseApplyDataVo> purchaseApplys = null;

        if (count > 0) {
            purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto), ApiPurchaseApplyDataVo.class);
        } else {
            purchaseApplys = new ArrayList<>();
        }

        ApiPurchaseApplyVo apiPurchaseApplyVo = new ApiPurchaseApplyVo();

        apiPurchaseApplyVo.setTotal(count);
        apiPurchaseApplyVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPurchaseApplyVo.setPurchaseApplys(purchaseApplys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPurchaseApplyVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
