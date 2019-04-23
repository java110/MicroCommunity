package com.java110.api.listener.floor;


import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.core.annotation.Java110Listener;
import com.java110.event.service.api.ServiceDataFlowEvent;
import org.springframework.http.HttpMethod;

@Java110Listener("QueryFloorsListener")
public class QueryFloorsListener extends AbstractServiceApiDataFlowListener {


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_FLOOR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }
}
