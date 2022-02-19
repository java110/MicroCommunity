package com.java110.core.trace;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import com.java110.dto.trace.TraceDto;
import com.java110.utils.constant.CommonConstant;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * restremplate 请求拦截器
 */
@Component
public class Java110RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(Java110RestTemplateInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.error("进入拦截器" + new String(body));
        TraceDto traceDto = Java110TraceFactory.getTraceDto();
        if (traceDto != null) {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.remove(CommonConstant.TRACE_ID);
            httpHeaders.remove(CommonConstant.PARENT_SPAN_ID);
            httpHeaders.add(CommonConstant.TRACE_ID, traceDto.getTraceId());
            httpHeaders.add(CommonConstant.PARENT_SPAN_ID, traceDto.getId());
        }
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        ClientHttpResponse clientHttpResponse = execution.execute(request, body);
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
        return clientHttpResponse;
    }
}
