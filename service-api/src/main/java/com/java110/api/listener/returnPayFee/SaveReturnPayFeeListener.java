package com.java110.api.listener.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.returnPayFee.IReturnPayFeeBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeReturnPayFeeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveReturnPayFeeListener")
public class SaveReturnPayFeeListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IReturnPayFeeBMO returnPayFeeBMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "reason", "请求报文中未包含reason");

        FeeDetailDto feeDetailDto =new FeeDetailDto();
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        feeDetailDto.setFeeId(reqJson.getString("feeId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        Assert.listOnlyOne(feeDetailDtos,"不存在该缴费记录");



    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setFeeId(reqJson.getString("feeId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos,"不存在该费用");


        reqJson.put("configId",feeDtos.get(0).getConfigId());

        returnPayFeeBMOImpl.addReturnPayFee(reqJson, context);
        reqJson.put("state","1000");
        returnPayFeeBMOImpl.updateFeeDetail(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeReturnPayFeeConstant.ADD_RETURNPAYFEE;
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
