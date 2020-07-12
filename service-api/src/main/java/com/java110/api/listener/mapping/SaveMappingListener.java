package com.java110.api.listener.mapping;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IMappingInnerServiceSMO;
import com.java110.dto.mapping.MappingDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeMappingConstant;
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
@Java110Listener("saveMappingListener")
public class SaveMappingListener extends AbstractServiceApiListener {


    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "domain", "必填，请填写域");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写名称");
        Assert.hasKeyAndValue(reqJson, "key", "必填，请填写键");
        Assert.hasKeyAndValue(reqJson, "value", "必填，请填写值");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.saveMapping(mappingDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "保存数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMappingConstant.ADD_MAPPING;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
}
