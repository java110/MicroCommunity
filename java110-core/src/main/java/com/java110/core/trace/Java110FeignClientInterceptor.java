package com.java110.core.trace;

import com.java110.core.factory.Java110TraceFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class Java110FeignClientInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(Java110FeignClientInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //before , request.body()
        logger.debug("feign 进入 Java110FeignClientAop>> intercept");
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        try {
            Response response = chain.proceed(request);
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
