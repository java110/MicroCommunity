package com.java110.api.listener.mapping;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.mapping.IMappingInnerServiceSMO;
import com.java110.dto.mapping.MappingDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.mapping.ApiMappingDataVo;
import com.java110.vo.api.mapping.ApiMappingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeMappingConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listMappingsListener")
public class ListMappingsListener extends AbstractServiceApiListener {

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeMappingConstant.LIST_MAPPINGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMappingInnerServiceSMO getMappingInnerServiceSMOImpl() {
        return mappingInnerServiceSMOImpl;
    }

    public void setMappingInnerServiceSMOImpl(IMappingInnerServiceSMO mappingInnerServiceSMOImpl) {
        this.mappingInnerServiceSMOImpl = mappingInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.queryMappingsCount(mappingDto);

        List<ApiMappingDataVo> mappings = null;

        if (count > 0) {
            mappings = BeanConvertUtil.covertBeanList(mappingInnerServiceSMOImpl.queryMappings(mappingDto), ApiMappingDataVo.class);
        } else {
            mappings = new ArrayList<>();
        }

        ApiMappingVo apiMappingVo = new ApiMappingVo();

        apiMappingVo.setTotal(count);
        apiMappingVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMappingVo.setMappings(mappings);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMappingVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
