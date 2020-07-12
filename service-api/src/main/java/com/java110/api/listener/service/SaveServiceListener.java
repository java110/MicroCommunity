package com.java110.api.listener.service;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IServiceInnerServiceSMO;
import com.java110.dto.service.ServiceDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeServiceConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveServiceListener")
public class SaveServiceListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceInnerServiceSMO serviceInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写服务名称");
        Assert.hasKeyAndValue(reqJson, "serviceCode", "必填，请填写服务编码如 service.saveService");
        Assert.hasKeyAndValue(reqJson, "businessTypeCd", "可填，请填写秘钥，如果填写了需要加密传输");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(reqJson, "isInstance", "可填，请填写实例 Y 或N");
        Assert.hasKeyAndValue(reqJson, "method", "必填，请填写调用方式");
        Assert.hasKeyAndValue(reqJson, "timeout", "必填，请填写超时时间");
        Assert.hasKeyAndValue(reqJson, "retryCount", "必填，请填写重试次数");
        Assert.hasKeyAndValue(reqJson, "provideAppId", "必填，请填写提供服务");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ServiceDto serviceDto = BeanConvertUtil.covertBean(reqJson, ServiceDto.class);

        serviceDto.setServiceId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_service_id));

        int count = serviceInnerServiceSMOImpl.saveService(serviceDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceConstant.ADD_SERVICE;
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
