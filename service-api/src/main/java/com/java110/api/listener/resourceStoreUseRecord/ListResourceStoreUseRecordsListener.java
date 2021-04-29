package com.java110.api.listener.resourceStoreUseRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.resourceStoreUseRecord.ResourceStoreUseRecordDto;
import com.java110.intf.store.IResourceStoreUseRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeResourceStoreUseRecordConstant;
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
@Java110Listener("listResourceStoreUseRecordsListener")
public class ListResourceStoreUseRecordsListener extends AbstractServiceApiListener {

    @Autowired
    private IResourceStoreUseRecordInnerServiceSMO resourceStoreUseRecordInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeResourceStoreUseRecordConstant.LIST_RESOURCESTOREUSERECORDS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IResourceStoreUseRecordInnerServiceSMO getResourceStoreUseRecordInnerServiceSMOImpl() {
        return resourceStoreUseRecordInnerServiceSMOImpl;
    }

    public void setResourceStoreUseRecordInnerServiceSMOImpl(IResourceStoreUseRecordInnerServiceSMO resourceStoreUseRecordInnerServiceSMOImpl) {
        this.resourceStoreUseRecordInnerServiceSMOImpl = resourceStoreUseRecordInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ResourceStoreUseRecordDto resourceStoreUseRecordDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreUseRecordDto.class);

        int count = resourceStoreUseRecordInnerServiceSMOImpl.queryResourceStoreUseRecordsCount(resourceStoreUseRecordDto);

        List<ResourceStoreUseRecordDto> resourceStoreUseRecordDtos = null;

        if (count > 0) {
            resourceStoreUseRecordDtos = resourceStoreUseRecordInnerServiceSMOImpl.queryResourceStoreUseRecords(resourceStoreUseRecordDto);
        } else {
            resourceStoreUseRecordDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreUseRecordDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
