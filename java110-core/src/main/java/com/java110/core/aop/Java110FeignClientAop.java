package com.java110.core.aop;

import com.java110.core.factory.Java110TraceFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.trace.TraceAnnotationsDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

@Aspect
public class Java110FeignClientAop {
    private static Logger logger = LoggerFactory.getLogger(Java110FeignClientAop.class);

    @Around("execution (* feign.Client.*(..)) && !within(is(FinalType))")
    public Object feignClientWasCalled(final ProceedingJoinPoint pjp) throws Throwable {

        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_SEND);
        Object clientHttpResponse = pjp.proceed();
        Java110TraceFactory.putAnnotations(TraceAnnotationsDto.VALUE_SERVER_RECEIVE);
        return clientHttpResponse;
    }

}
