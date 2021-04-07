package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.util.BeanConvertUtil;
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
@Java110Listener("listAllocationStorehousesListener")
public class ListAllocationStorehousesListener extends AbstractServiceApiListener {

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseConstant.LIST_ALLOCATIONSTOREHOUSES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAllocationStorehouseInnerServiceSMO getAllocationStorehouseInnerServiceSMOImpl() {
        return allocationStorehouseInnerServiceSMOImpl;
    }

    public void setAllocationStorehouseInnerServiceSMOImpl(IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl) {
        this.allocationStorehouseInnerServiceSMOImpl = allocationStorehouseInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AllocationStorehouseDto allocationStorehouseDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseDto.class);

        int count = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehousesCount(allocationStorehouseDto);

        List<AllocationStorehouseDto> allocationStorehouseDtos = null;

        if (count > 0) {
            allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        } else {
            allocationStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
