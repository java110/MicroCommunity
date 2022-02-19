package com.java110.core.aop;

import com.java110.core.factory.Java110TraceFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;

import java.io.IOException;


public class Java110FeignClientAop implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(Java110FeignClientAop.class);

    //    @Around("execution (* feign.Client.*(..)) && !within(is(FinalType))")
//    public Object feignClientWasCalled(final ProceedingJoinPoint pjp) throws Throwable {
//
//        logger.debug("feign 进入 Java110FeignClientAop>> feignClientWasCalled");
//
//        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
//        Object clientHttpResponse = pjp.proceed();
//        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
//        return clientHttpResponse;
//    }
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
