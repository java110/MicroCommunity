package com.java110.api.listener.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeServiceRegisterConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.IRouteInnerServiceSMO;
import com.java110.dto.service.RouteDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteServiceRegisterListener")
public class DeleteServiceRegisterListener extends AbstractServiceApiListener {

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "id", "绑定ID不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);

        routeDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);

        int count = routeInnerServiceSMOImpl.deleteRoute(routeDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "删除数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceRegisterConstant.DELETE_SERVICEREGISTER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRouteInnerServiceSMO getRouteInnerServiceSMOImpl() {
        return routeInnerServiceSMOImpl;
    }

    public void setRouteInnerServiceSMOImpl(IRouteInnerServiceSMO routeInnerServiceSMOImpl) {
        this.routeInnerServiceSMOImpl = routeInnerServiceSMOImpl;
    }
}
