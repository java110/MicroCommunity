package com.java110.api.listener.service;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.service.IServiceInnerServiceSMO;
import com.java110.dto.service.ServiceDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.service.ApiServiceDataVo;
import com.java110.vo.api.service.ApiServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeServiceConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listServicesListener")
public class ListServicesListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceConstant.LIST_SERVICES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IServiceInnerServiceSMO getServiceInnerServiceSMOImpl() {
        return serviceInnerServiceSMOImpl;
    }

    public void setServiceInnerServiceSMOImpl(IServiceInnerServiceSMO serviceInnerServiceSMOImpl) {
        this.serviceInnerServiceSMOImpl = serviceInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ServiceDto serviceDto = BeanConvertUtil.covertBean(reqJson, ServiceDto.class);

        int count = serviceInnerServiceSMOImpl.queryServicesCount(serviceDto);

        List<ApiServiceDataVo> services = null;

        if (count > 0) {
            services = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServices(serviceDto), ApiServiceDataVo.class);
        } else {
            services = new ArrayList<>();
        }

        ApiServiceVo apiServiceVo = new ApiServiceVo();

        apiServiceVo.setTotal(count);
        apiServiceVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceVo.setServices(services);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
