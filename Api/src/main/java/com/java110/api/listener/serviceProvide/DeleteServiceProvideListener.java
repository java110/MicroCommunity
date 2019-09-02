package com.java110.api.listener.serviceProvide;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.service.IServiceInnerServiceSMO;
import com.java110.dto.service.ServiceProvideDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.BusinessTypeConstant;

import com.java110.core.annotation.Java110Listener;
import com.java110.common.constant.ServiceCodeServiceProvideConstant;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteServiceProvideListener")
public class DeleteServiceProvideListener extends AbstractServiceApiListener {



    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "id", "提供ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ServiceProvideDto serviceProvideDto = BeanConvertUtil.covertBean(reqJson, ServiceProvideDto.class);

        int count = serviceInnerServiceSMOImpl.deleteServiceProvide(serviceProvideDto);
        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "删除数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceProvideConstant.DELETE_SERVICEPROVIDE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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

}
