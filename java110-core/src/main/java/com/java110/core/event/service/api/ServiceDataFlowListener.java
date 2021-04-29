package com.java110.core.event.service.api;

import com.java110.core.event.app.order.Ordered;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.util.EventListener;

/**
 * 通用事件处理，
 * Created by wuxw on 2018/4/17.
 */
public interface ServiceDataFlowListener extends EventListener,Ordered {

    /**
     * 业务 编码
     * @return
     */
    public String getServiceCode();

    /**
     * 获取调用时的方法
     * @return 接口对外提供方式 如HttpMethod.POST
     */
    public HttpMethod getHttpMethod();

    public void soService(ServiceDataFlowEvent event) throws ParseException;
}
