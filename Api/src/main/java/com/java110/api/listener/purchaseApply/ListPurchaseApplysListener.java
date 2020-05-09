package com.java110.api.listener.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.purchaseApply.IPurchaseApplyInnerServiceSMO;
import com.java110.core.smo.purchaseApplyUser.IPurchaseApplyUserInnerServiceSMO;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.purchaseApply.PurchaseApplyDetailDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodePurchaseApplyConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyDataVo;
import com.java110.vo.api.purchaseApply.ApiPurchaseApplyVo;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listPurchaseApplysListener")
public class ListPurchaseApplysListener extends AbstractServiceApiListener {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写订单类型");
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDto.class);

        int count = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplysCount(purchaseApplyDto);

        List<ApiPurchaseApplyDataVo> purchaseApplys = null;
        if (count > 0) {
            List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplyAndDetails(purchaseApplyDto);
            purchaseApplyDtos = freshCurrentUser(purchaseApplyDtos);
            purchaseApplys = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiPurchaseApplyDataVo.class);
            for( ApiPurchaseApplyDataVo apiPurchaseApplyDataVo : purchaseApplys){
                List<PurchaseApplyDetailVo> applyDetailList = apiPurchaseApplyDataVo.getPurchaseApplyDetailVo();
                if(applyDetailList.size() > 0){
                    StringBuffer resNames = new StringBuffer();
                    BigDecimal totalPrice = new BigDecimal(0);
                    for( PurchaseApplyDetailVo purchaseApplyDetailVo : applyDetailList){
                        resNames.append(purchaseApplyDetailVo.getResName()+";");
                        BigDecimal price = new BigDecimal(purchaseApplyDetailVo.getPrice());
                        BigDecimal quantity = new BigDecimal(purchaseApplyDetailVo.getQuantity());
                        totalPrice = totalPrice.add(price.multiply(quantity));
                    }
                    apiPurchaseApplyDataVo.setResourceNames(resNames.toString());
                    apiPurchaseApplyDataVo.setTotalPrice(totalPrice.toString());
                }

            }

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


    private List<PurchaseApplyDto>  freshCurrentUser(List<PurchaseApplyDto> purchaseApplyDtos) {
        List<PurchaseApplyDto> tmpPurchaseApplyDtos = new ArrayList<>();
        for(PurchaseApplyDto purchaseApplyDto : purchaseApplyDtos){
            purchaseApplyDto = purchaseApplyUserInnerServiceSMOImpl.getTaskCurrentUser(purchaseApplyDto);
            tmpPurchaseApplyDtos.add(purchaseApplyDto);
        }

        return tmpPurchaseApplyDtos;
    }
}
