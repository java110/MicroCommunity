package com.java110.api.listener.meterWater;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.utils.constant.ServiceCodeMeterWaterConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
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
@Java110Listener("listMeterWatersListener")
public class ListMeterWatersListener extends AbstractServiceApiListener {

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMeterWaterConstant.LIST_METERWATERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMeterWaterInnerServiceSMO getMeterWaterInnerServiceSMOImpl() {
        return meterWaterInnerServiceSMOImpl;
    }

    public void setMeterWaterInnerServiceSMOImpl(IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl) {
        this.meterWaterInnerServiceSMOImpl = meterWaterInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MeterWaterDto meterWaterDto = BeanConvertUtil.covertBean(reqJson, MeterWaterDto.class);

        int count = meterWaterInnerServiceSMOImpl.queryMeterWatersCount(meterWaterDto);

        List<MeterWaterDto> meterWaterDtos = null;

        if (count > 0) {
            meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
        } else {
            meterWaterDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, meterWaterDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
