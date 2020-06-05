package com.java110.api.listener.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeServiceRegisterConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IRouteInnerServiceSMO;
import com.java110.dto.service.RouteDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.serviceRegister.ApiServiceRegisterDataVo;
import com.java110.vo.api.serviceRegister.ApiServiceRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listServiceRegistersListener")
public class ListServiceRegistersListener extends AbstractServiceApiListener {

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOIMpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceRegisterConstant.LIST_SERVICEREGISTERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IRouteInnerServiceSMO getRouteInnerServiceSMOIMpl() {
        return routeInnerServiceSMOIMpl;
    }

    public void setRouteInnerServiceSMOIMpl(IRouteInnerServiceSMO routeInnerServiceSMOIMpl) {
        this.routeInnerServiceSMOIMpl = routeInnerServiceSMOIMpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);

        int count = routeInnerServiceSMOIMpl.queryRoutesCount(routeDto);

        List<ApiServiceRegisterDataVo> serviceRegisters = null;

        if (count > 0) {
            serviceRegisters = BeanConvertUtil.covertBeanList(routeInnerServiceSMOIMpl.queryRoutes(routeDto), ApiServiceRegisterDataVo.class);
        } else {
            serviceRegisters = new ArrayList<>();
        }

        ApiServiceRegisterVo apiServiceRegisterVo = new ApiServiceRegisterVo();

        apiServiceRegisterVo.setTotal(count);
        apiServiceRegisterVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiServiceRegisterVo.setServiceRegisters(serviceRegisters);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiServiceRegisterVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
