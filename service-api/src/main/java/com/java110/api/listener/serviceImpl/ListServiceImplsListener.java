package com.java110.api.listener.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeServiceImplConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IServiceBusinessInnerServiceSMO;
import com.java110.dto.service.ServiceBusinessDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.serviceImpl.ApiServiceImplDataVo;
import com.java110.vo.api.serviceImpl.ApiServiceImplVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listServiceImplsListener")
public class ListServiceImplsListener extends AbstractServiceApiListener {

    @Autowired
    private IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceImplConstant.LIST_SERVICEIMPLS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }



    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ServiceBusinessDto serviceImplDto = BeanConvertUtil.covertBean(reqJson, ServiceBusinessDto.class);

        int count = serviceBusinessInnerServiceSMOImpl.queryServiceBusinesssCount(serviceImplDto);

        List<ApiServiceImplDataVo> serviceImpls = null;

        if (count > 0) {
            serviceImpls = BeanConvertUtil.covertBeanList(serviceBusinessInnerServiceSMOImpl.queryServiceBusinesss(serviceImplDto), ApiServiceImplDataVo.class);
        } else {
            serviceImpls = new ArrayList<>();
        }

        ApiServiceImplVo apiServiceImplVo = new ApiServiceImplVo();

        apiServiceImplVo.setTotal(count);
        apiServiceImplVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceImplVo.setServiceImpls(serviceImpls);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceImplVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    public IServiceBusinessInnerServiceSMO getServiceBusinessInnerServiceSMOImpl() {
        return serviceBusinessInnerServiceSMOImpl;
    }

    public void setServiceBusinessInnerServiceSMOImpl(IServiceBusinessInnerServiceSMO serviceBusinessInnerServiceSMOImpl) {
        this.serviceBusinessInnerServiceSMOImpl = serviceBusinessInnerServiceSMOImpl;
    }
}
