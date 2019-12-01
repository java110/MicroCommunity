package com.java110.api.listener.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IApplicationKeyInnerServiceSMO;
import com.java110.dto.hardwareAdapation.ApplicationKeyDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeApplicationKeyConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.applicationKey.ApiApplicationKeyDataVo;
import com.java110.vo.api.applicationKey.ApiApplicationKeyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listApplicationKeysListener")
public class ListApplicationKeysListener extends AbstractServiceApiListener {

    @Autowired
    private IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeApplicationKeyConstant.LIST_APPLICATIONKEYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IApplicationKeyInnerServiceSMO getApplicationKeyInnerServiceSMOImpl() {
        return applicationKeyInnerServiceSMOImpl;
    }

    public void setApplicationKeyInnerServiceSMOImpl(IApplicationKeyInnerServiceSMO applicationKeyInnerServiceSMOImpl) {
        this.applicationKeyInnerServiceSMOImpl = applicationKeyInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ApplicationKeyDto applicationKeyDto = BeanConvertUtil.covertBean(reqJson, ApplicationKeyDto.class);

        int count = applicationKeyInnerServiceSMOImpl.queryApplicationKeysCount(applicationKeyDto);

        List<ApiApplicationKeyDataVo> applicationKeys = null;

        if (count > 0) {
            applicationKeys = BeanConvertUtil.covertBeanList(applicationKeyInnerServiceSMOImpl.queryApplicationKeys(applicationKeyDto), ApiApplicationKeyDataVo.class);
        } else {
            applicationKeys = new ArrayList<>();
        }

        ApiApplicationKeyVo apiApplicationKeyVo = new ApiApplicationKeyVo();

        apiApplicationKeyVo.setTotal(count);
        apiApplicationKeyVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiApplicationKeyVo.setApplicationKeys(applicationKeys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiApplicationKeyVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
