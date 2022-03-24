package com.java110.core.trace;

import com.java110.dto.trace.TraceAnnotationsDto;
import com.java110.dto.trace.TraceDto;
import com.java110.utils.constant.CommonConstant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

//@Component
public class Java110FeignClientInterceptor implements Interceptor {
    // private static Logger logger = LoggerFactory.getLogger(Java110FeignClientInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Request request = chain.request();

        Request.Builder builder = chain.request().newBuilder();
        //调用链头信息
        TraceDto traceDto = Java110TraceFactory.getTraceDto();
        if (traceDto != null) {
            builder.header(CommonConstant.TRACE_ID, traceDto.getTraceId());
            builder.header(CommonConstant.PARENT_SPAN_ID, traceDto.getId());
        }
        //logger.debug("feign 进入 Java110FeignClientAop>> intercept");
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        try {
            Response response = chain.proceed(builder.build());
            //after
            return response;
        } catch (Exception e) {
            //log error
            throw e;
        } finally {
            //clean up
            Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
        }
    }


}
