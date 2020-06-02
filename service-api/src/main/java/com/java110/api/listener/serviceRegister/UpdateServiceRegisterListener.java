package com.java110.api.listener.serviceRegister;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.service.IRouteInnerServiceSMO;
import com.java110.dto.service.RouteDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.ServiceCodeServiceRegisterConstant;

/**
 * 保存服务绑定侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("updateServiceRegisterListener")
public class UpdateServiceRegisterListener extends AbstractServiceApiListener {

    @Autowired
    private IRouteInnerServiceSMO routeInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "id", "绑定ID不能为空");
        Assert.hasKeyAndValue(reqJson, "appId", "必填，请填写应用ID");
        Assert.hasKeyAndValue(reqJson, "serviceId", "必填，请填写服务ID");
        Assert.hasKeyAndValue(reqJson, "orderTypeCd", "必填，请填写订单类型");
        Assert.hasKeyAndValue(reqJson, "invokeLimitTimes", "必填，请填写调用次数");
        Assert.hasKeyAndValue(reqJson, "invokeModel", "可填，请填写消息队列，订单在异步调用时使用");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RouteDto routeDto = BeanConvertUtil.covertBean(reqJson, RouteDto.class);


        int count = routeInnerServiceSMOImpl.updateRoute(routeDto);


        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    public IRouteInnerServiceSMO getRouteInnerServiceSMOImpl() {
        return routeInnerServiceSMOImpl;
    }

    public void setRouteInnerServiceSMOImpl(IRouteInnerServiceSMO routeInnerServiceSMOImpl) {
        this.routeInnerServiceSMOImpl = routeInnerServiceSMOImpl;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeServiceRegisterConstant.UPDATE_SERVICEREGISTER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


}
