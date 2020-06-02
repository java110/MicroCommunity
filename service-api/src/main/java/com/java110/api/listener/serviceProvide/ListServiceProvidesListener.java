package com.java110.api.listener.serviceProvide;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeServiceProvideConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.service.IServiceInnerServiceSMO;
import com.java110.dto.service.ServiceProvideDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.serviceProvide.ApiServiceProvideDataVo;
import com.java110.vo.api.serviceProvide.ApiServiceProvideVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listServiceProvidesListener")
public class ListServiceProvidesListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceProvideConstant.LIST_SERVICEPROVIDES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ServiceProvideDto serviceProvideDto = BeanConvertUtil.covertBean(reqJson, ServiceProvideDto.class);

        int count = serviceInnerServiceSMOImpl.queryServiceProvidesCount(serviceProvideDto);

        List<ApiServiceProvideDataVo> serviceProvides = null;

        if (count > 0) {
            serviceProvides = BeanConvertUtil.covertBeanList(serviceInnerServiceSMOImpl.queryServiceProvides(serviceProvideDto), ApiServiceProvideDataVo.class);
        } else {
            serviceProvides = new ArrayList<>();
        }

        ApiServiceProvideVo apiServiceProvideVo = new ApiServiceProvideVo();

        apiServiceProvideVo.setTotal(count);
        apiServiceProvideVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceProvideVo.setServiceProvides(serviceProvides);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceProvideVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IServiceInnerServiceSMO getServiceInnerServiceSMOImpl() {
        return serviceInnerServiceSMOImpl;
    }

    public void setServiceInnerServiceSMOImpl(IServiceInnerServiceSMO serviceInnerServiceSMOImpl) {
        this.serviceInnerServiceSMOImpl = serviceInnerServiceSMOImpl;
    }
}
