package com.java110.api.listener.resourceSupplier;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.resourceSupplier.ResourceSupplierDto;
import com.java110.intf.store.IResourceSupplierInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeResourceSupplierConstant;
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
@Java110Listener("listResourceSuppliersListener")
public class ListResourceSuppliersListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceSupplierInnerServiceSMO resourceSupplierInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceSupplierConstant.LIST_RESOURCESUPPLIERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IResourceSupplierInnerServiceSMO getResourceSupplierInnerServiceSMOImpl() {
        return resourceSupplierInnerServiceSMOImpl;
    }

    public void setResourceSupplierInnerServiceSMOImpl(IResourceSupplierInnerServiceSMO resourceSupplierInnerServiceSMOImpl) {
        this.resourceSupplierInnerServiceSMOImpl = resourceSupplierInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResourceSupplierDto resourceSupplierDto = BeanConvertUtil.covertBean(reqJson, ResourceSupplierDto.class);

        int count = resourceSupplierInnerServiceSMOImpl.queryResourceSuppliersCount(resourceSupplierDto);

        List<ResourceSupplierDto> resourceSupplierDtos = null;

        if (count > 0) {
            resourceSupplierDtos = resourceSupplierInnerServiceSMOImpl.queryResourceSuppliers(resourceSupplierDto);
        } else {
            resourceSupplierDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceSupplierDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
