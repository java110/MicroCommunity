package com.java110.api.listener.payFeeDetailDiscount;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.utils.constant.ServiceCodePayFeeDetailDiscountConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listPayFeeDetailDiscountsListener")
public class ListPayFeeDetailDiscountsListener extends AbstractServiceApiListener {

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePayFeeDetailDiscountConstant.LIST_PAYFEEDETAILDISCOUNTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IPayFeeDetailDiscountInnerServiceSMO getPayFeeDetailDiscountInnerServiceSMOImpl() {
        return payFeeDetailDiscountInnerServiceSMOImpl;
    }

    public void setPayFeeDetailDiscountInnerServiceSMOImpl(IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl) {
        this.payFeeDetailDiscountInnerServiceSMOImpl = payFeeDetailDiscountInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        PayFeeDetailDiscountDto payFeeDetailDiscountDto = BeanConvertUtil.covertBean(reqJson, PayFeeDetailDiscountDto.class);

        int count = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscountsCount(payFeeDetailDiscountDto);

        List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = null;

        if (count > 0) {
            payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscounts(payFeeDetailDiscountDto);
        } else {
            payFeeDetailDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, payFeeDetailDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
