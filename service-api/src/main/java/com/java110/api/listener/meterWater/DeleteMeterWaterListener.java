package com.java110.api.listener.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.meterWater.IMeterWaterBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeMeterWaterConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;


/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteMeterWaterListener")
public class DeleteMeterWaterListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IMeterWaterBMO meterWaterBMOImpl;

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "waterId", "waterId不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        MeterWaterDto meterWaterDto = new MeterWaterDto();
        meterWaterDto.setWaterId(reqJson.getString("waterId"));
        meterWaterDto.setCommunityId(reqJson.getString("communityId"));
        List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);

        Assert.listOnlyOne(meterWaterDtos, "数据异常未找到费用信息");

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(meterWaterDtos.get(0).getFeeId());
        payFeePo.setCommunityId(meterWaterDtos.get(0).getCommunityId());
        super.delete(context, payFeePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FEE_INFO);
        meterWaterBMOImpl.deleteMeterWater(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMeterWaterConstant.DELETE_METERWATER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
