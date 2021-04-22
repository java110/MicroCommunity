package com.java110.api.listener.resourceStoreType;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import com.java110.utils.constant.ServiceCodeResourceStoreTypeConstant;
import com.java110.intf.store.IResourceStoreTypeInnerServiceSMO;
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
@Java110Listener("listResourceStoreTypesListener")
public class ListResourceStoreTypesListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceStoreTypeInnerServiceSMO resourceStoreTypeInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreTypeConstant.LIST_RESOURCESTORETYPES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IResourceStoreTypeInnerServiceSMO getResourceStoreTypeInnerServiceSMOImpl() {
        return resourceStoreTypeInnerServiceSMOImpl;
    }

    public void setResourceStoreTypeInnerServiceSMOImpl(IResourceStoreTypeInnerServiceSMO resourceStoreTypeInnerServiceSMOImpl) {
        this.resourceStoreTypeInnerServiceSMOImpl = resourceStoreTypeInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResourceStoreTypeDto resourceStoreTypeDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypeDto.class);

        int count = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypesCount(resourceStoreTypeDto);

        List<ResourceStoreTypeDto> resourceStoreTypeDtos = null;

        if (count > 0) {
            resourceStoreTypeDtos = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
        } else {
            resourceStoreTypeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
