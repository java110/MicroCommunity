package com.java110.api.listener.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IServiceBusinessInnerServiceSMO;
import com.java110.dto.service.ServiceBusinessDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.java110.core.annotation.Java110Listener;
import com.java110.utils.constant.ServiceCodeServiceImplConstant;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteServiceImplListener")
public class DeleteServiceImplListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "serviceBusinessId", "服务实现ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        ServiceBusinessDto serviceImplDto = BeanConvertUtil.covertBean(reqJson, ServiceBusinessDto.class);


        int saveFlag = serviceBusinessInnerServiceSMOImpl.deleteServiceBusiness(serviceImplDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceImplConstant.DELETE_SERVICEIMPL;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IServiceBusinessInnerServiceSMO getServiceBusinessInnerServiceSMOImpl() {
        return serviceBusinessInnerServiceSMOImpl;
    }

    public void setServiceBusinessInnerServiceSMOImpl(IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl) {
        this.serviceBusinessInnerServiceSMOImpl = serviceBusinessInnerServiceSMOImpl;
    }
}
