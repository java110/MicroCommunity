package com.java110.api.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.resourceStoreSpecification.ResourceStoreSpecificationDto;
import com.java110.intf.store.IResourceStoreSpecificationInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeResourceStoreSpecificationConstant;
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
@Java110Listener("listResourceStoreSpecificationsListener")
public class ListResourceStoreSpecificationsListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceStoreSpecificationInnerServiceSMO resourceStoreSpecificationInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreSpecificationConstant.LIST_RESOURCESTORESPECIFICATIONS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IResourceStoreSpecificationInnerServiceSMO getResourceStoreSpecificationInnerServiceSMOImpl() {
        return resourceStoreSpecificationInnerServiceSMOImpl;
    }

    public void setResourceStoreSpecificationInnerServiceSMOImpl(IResourceStoreSpecificationInnerServiceSMO resourceStoreSpecificationInnerServiceSMOImpl) {
        this.resourceStoreSpecificationInnerServiceSMOImpl = resourceStoreSpecificationInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResourceStoreSpecificationDto resourceStoreSpecificationDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreSpecificationDto.class);

        int count = resourceStoreSpecificationInnerServiceSMOImpl.queryResourceStoreSpecificationsCount(resourceStoreSpecificationDto);

        List<ResourceStoreSpecificationDto> resourceStoreSpecificationDtos = null;

        if (count > 0) {
            resourceStoreSpecificationDtos = resourceStoreSpecificationInnerServiceSMOImpl.queryResourceStoreSpecifications(resourceStoreSpecificationDto);
        } else {
            resourceStoreSpecificationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreSpecificationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
