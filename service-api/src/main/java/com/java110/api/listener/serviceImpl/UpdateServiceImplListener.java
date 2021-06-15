package com.java110.api.listener.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IServiceBusinessInnerServiceSMO;
import com.java110.dto.service.ServiceBusinessDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeServiceImplConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存服务实现侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateServiceImplListener")
public class UpdateServiceImplListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "serviceBusinessId", "必填，请填写应用ID");
        Assert.hasKeyAndValue(reqJson, "businessTypeCd", "必填，请填写业务类型");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写业务名称");
        Assert.hasKeyAndValue(reqJson, "invokeType", "必填，请填写调用类型");
        Assert.hasKeyAndValue(reqJson, "url", "必填，请填写调用地址，为mapping 表中domain为DOMAIN.COMMON映射key");
        Assert.hasKeyAndValue(reqJson, "timeout", "必填，请填写超时时间");
        Assert.hasKeyAndValue(reqJson, "retryCount", "必填，请填写重试次数");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResponseEntity<String> responseEntity = null;

        ServiceBusinessDto serviceImplDto = BeanConvertUtil.covertBean(reqJson, ServiceBusinessDto.class);


        int saveFlag = serviceBusinessInnerServiceSMOImpl.updateServiceBusiness(serviceImplDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceImplConstant.UPDATE_SERVICEIMPL;
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
