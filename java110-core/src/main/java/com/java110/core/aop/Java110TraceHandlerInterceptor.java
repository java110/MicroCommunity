package com.java110.core.aop;

import com.java110.core.factory.Java110TraceFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有请求 调用链 拦截
 * Created by wuxw on 2018/5/2.
 */
@Component
public class Java110TraceHandlerInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(Java110TraceHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("进入拦截器Java110TraceHandlerInterceptor>>preHandle");
        // 获取组件名称 和方法名称
        String url = request.getRequestURL() != null ? request.getRequestURL().toString() : "";
        Map<String, Object> headers = new HashMap<>();
        Enumeration reqHeaderEnum = request.getHeaderNames();
        while (reqHeaderEnum.hasMoreElements()) {
            String headerName = (String) reqHeaderEnum.nextElement();
            headers.put(headerName.toLowerCase(), request.getHeader(headerName));
        }
        //调用链
        Java110TraceFactory.createTrace(url, headers);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("完成拦截器Java110TraceHandlerInterceptor>>afterCompletion");
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_CLIENT_RECEIVE);
    }


}
